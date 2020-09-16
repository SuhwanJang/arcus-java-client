package com.jam2in.arcus.admin.tool.domain.agent.service;

import com.jam2in.arcus.admin.tool.domain.agent.component.AdminAgentComponent;
import com.jam2in.arcus.admin.tool.domain.agent.dto.AdminAgentDto;
import com.jam2in.arcus.admin.tool.domain.agent.dto.MemcachedOptionsDto;
import com.jam2in.arcus.admin.tool.domain.agent.entity.AdminAgentEntity;
import com.jam2in.arcus.admin.tool.domain.agent.entity.MemcachedOptionsEntity;
import com.jam2in.arcus.admin.tool.domain.agent.repository.AdminAgentRepository;
import com.jam2in.arcus.admin.tool.domain.agent.repository.MemcachedOptionsRepository;
import com.jam2in.arcus.admin.tool.domain.zookeeper.dto.ZooKeeperParticipantsDto;
import com.jam2in.arcus.admin.tool.domain.zookeeper.dto.ZooKeeperDto;
import com.jam2in.arcus.admin.tool.error.ApiError;
import com.jam2in.arcus.admin.tool.error.ApiErrorCode;
import com.jam2in.arcus.admin.tool.exception.BusinessException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class AdminAgentService {

  private final AdminAgentRepository adminAgentRepository;
  private final AdminAgentComponent adminAgentComponent;
  private final MemcachedOptionsRepository memcachedOptionsRepository;

  public AdminAgentService(AdminAgentRepository adminAgentRepository,
                           AdminAgentComponent adminAgentComponent,
                           MemcachedOptionsRepository memcachedOptionsRepository) {
    this.adminAgentRepository = adminAgentRepository;
    this.adminAgentComponent = adminAgentComponent;
    this.memcachedOptionsRepository = memcachedOptionsRepository;
  }

  @Transactional
  public AdminAgentDto create(AdminAgentDto adminAgentDto) {
    Optional<AdminAgentEntity> optional = adminAgentRepository.findByIp(adminAgentDto.getIp());
    AdminAgentEntity adminAgentEntity;

    if (optional.isPresent()) {
      adminAgentEntity = optional.get();
      adminAgentEntity.setPort(adminAgentDto.getPort());
      adminAgentEntity.setToken(adminAgentDto.getToken());
    } else {
      adminAgentEntity = AdminAgentEntity.of(adminAgentDto);
    }

    adminAgentRepository.save(adminAgentEntity);

    return AdminAgentDto.of(adminAgentEntity);
  }

  public void installZooKeeperServer(String address, String version) {
    IpPort ipPort = new IpPort(address);
    AdminAgentEntity adminAgentEntity = getEntityByIp(ipPort.ip);

    adminAgentComponent.installZooKeeperServer(
        adminAgentEntity.getAddress(),
        ipPort.port,
        version,
        adminAgentEntity.getToken());
  }

  public void initializeZooKeeperServer(List<ZooKeeperDto> zookeeperDtos, String zkservers) {
    Map<Integer, String> notFoundAgentIpMap = new HashMap<>();
    Map<String, AdminAgentEntity> adminAgentMap = new HashMap<>();

    // initialize agent map.
    for (int i = 0; i < CollectionUtils.size(zookeeperDtos); i++) {
      ZooKeeperDto zookeeperDto = zookeeperDtos.get(i);

      try {
        adminAgentMap.put(zookeeperDto.getIp(), getEntityByIp(zookeeperDto.getIp()));
      } catch (BusinessException e) {
        notFoundAgentIpMap.put(i, zookeeperDto.getIp());
      }
    }

    // throw exception if some of agents are not found.
    if (!notFoundAgentIpMap.isEmpty()) {
      throw new BusinessException(
          ApiError.of(
              ApiErrorCode.AGENT_NOT_FOUND,
              MapUtils.emptyIfNull(notFoundAgentIpMap).entrySet()
                  .stream()
                  .map(entry -> ApiError.Detail.of(String.format("zkservers[%d].ip", entry.getKey()), entry.getValue()))
                  .collect(Collectors.toList())));
    }

    // request for zookeeper config initialization.
    List<ApiError> apiErrors = CollectionUtils.emptyIfNull(zookeeperDtos)
        .stream()
        .map(zookeeperDto -> {
          AdminAgentEntity adminAgentEntity = adminAgentMap.get(zookeeperDto.getIp());
          return adminAgentComponent.initializeZooKeeperServer(
              adminAgentEntity.getAddress(), zookeeperDto.getClientPort(),
              new ZooKeeperParticipantsDto(zookeeperDto.getMyId(), zkservers),
              adminAgentEntity.getToken());
        })
        .collect(Collectors.toList())
        .stream()
        .map(CompletableFuture::join)
        .collect(Collectors.toList());

    // if some of the requests have failed, throw exception
    List<ApiError.Detail> details = new LinkedList<>();

    for (int i = 0; i < CollectionUtils.size(apiErrors); i++) {
      ZooKeeperDto zookeeperDto = zookeeperDtos.get(i);
      ApiError apiError = apiErrors.get(i);

      if (apiError != null) {
        details.add(
            ApiError.Detail.of(
                String.format("zkservers[%d].ip", i),
                zookeeperDto.getIp(),
                apiError.getCode()));
      }
    }

    if (!details.isEmpty()) {
      throw new BusinessException(ApiError.of(ApiErrorCode.ENSEMBLE_INITIALIZATION_FAILURE, details));
    }
  }

  public void startZooKeeperServer(String address) {
    IpPort ipPort = new IpPort(address);
    AdminAgentEntity adminAgentEntity = getEntityByIp(ipPort.ip);

    adminAgentComponent.startZooKeeperServer(
        adminAgentEntity.getAddress(),
        ipPort.port,
        adminAgentEntity.getToken());
  }

  public void stopZooKeeperServer(String address) {
    IpPort ipPort = new IpPort(address);
    AdminAgentEntity adminAgentEntity = getEntityByIp(ipPort.ip);

    adminAgentComponent.stopZooKeeperServer(
        adminAgentEntity.getIp() + ":" + adminAgentEntity.getPort(),
        ipPort.port,
        adminAgentEntity.getToken());
  }

  public void installMemcachedServer(String address, String version) {
    IpPort ipPort = new IpPort(address);
    AdminAgentEntity adminAgentEntity = getEntityByIp(ipPort.ip);

    adminAgentComponent.installMemcachedServer(
        adminAgentEntity.getIp() + ":" + adminAgentEntity.getPort(),
        ipPort.port,
        version,
        adminAgentEntity.getToken());
  }

  @Transactional
  public void startMemcachedServer(String address, MemcachedOptionsDto memcachedOptionsDto) {
    IpPort ipPort = new IpPort(address);
    AdminAgentEntity adminAgentEntity = getEntityByIp(ipPort.ip);

    adminAgentComponent.startMemcachedServer(
        adminAgentEntity.getAddress(),
        ipPort.port,
        memcachedOptionsDto,
        adminAgentEntity.getToken());

    memcachedOptionsRepository.save(MemcachedOptionsEntity.of(memcachedOptionsDto, address));
  }

  public MemcachedOptionsDto getMemcachedOptions(String address) {
    return MemcachedOptionsDto.of(
        memcachedOptionsRepository.findById(address)
            .orElseThrow(() -> new BusinessException(ApiError.of(ApiErrorCode.AGENT_CACHE_OPTION_NOT_FOUND))));
  }

  public void stopMemcachedServer(String address) {
    IpPort ipPort = new IpPort(address);
    AdminAgentEntity adminAgentEntity = getEntityByIp(ipPort.ip);

    adminAgentComponent.stopMemcachedServer(
        adminAgentEntity.getAddress(),
        ipPort.port,
        adminAgentEntity.getToken());
  }

  private AdminAgentEntity getEntityByIp(String ip) {
    return adminAgentRepository.findByIp(ip)
        .orElseThrow(() -> new BusinessException(ApiError.of(ApiErrorCode.AGENT_NOT_FOUND)));
  }

  private static class IpPort {

    private final String ip;

    private final int port;

    IpPort(String address) {
      String[] splitted = address.split(":", 2);

      ip = splitted[0];
      port = Integer.parseInt(splitted[1]);
    }

  }

}
