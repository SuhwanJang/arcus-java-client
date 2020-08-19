package com.jam2in.arcus.admin.tool.domain.agent.service;

import com.jam2in.arcus.admin.tool.domain.agent.component.AdminAgentComponent;
import com.jam2in.arcus.admin.tool.domain.agent.dto.AdminAgentDto;
import com.jam2in.arcus.admin.tool.domain.agent.dto.MemcachedOptionsDto;
import com.jam2in.arcus.admin.tool.domain.agent.entity.AdminAgentEntity;
import com.jam2in.arcus.admin.tool.domain.agent.entity.MemcachedOptionsEntity;
import com.jam2in.arcus.admin.tool.domain.agent.repository.AdminAgentRepository;
import com.jam2in.arcus.admin.tool.domain.agent.repository.MemcachedOptionsRepository;
import com.jam2in.arcus.admin.tool.domain.common.validator.AddressValidator;
import com.jam2in.arcus.admin.tool.error.ApiErrorCode;
import com.jam2in.arcus.admin.tool.exception.BusinessException;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Optional;

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
      adminAgentEntity.updatePort(adminAgentDto.getPort());
      adminAgentEntity.updateToken(adminAgentDto.getToken());
    } else {
      adminAgentEntity = AdminAgentEntity.of(adminAgentDto);
    }

    adminAgentRepository.save(adminAgentEntity);

    return AdminAgentDto.of(adminAgentEntity);
  }

  public void startZooKeeperServer(String address) {
    IpPort ipPort = new IpPort(address);
    AdminAgentEntity adminAgentEntity = getEntityByIp(ipPort.ip);

    adminAgentComponent.startZooKeeperServer(
        adminAgentEntity.getIp() + ":" + adminAgentEntity.getPort(),
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

  @Transactional
  public void startMemcachedServer(String address, MemcachedOptionsDto memcachedOptionsDto) {
    IpPort ipPort = new IpPort(address);
    AdminAgentEntity adminAgentEntity = getEntityByIp(ipPort.ip);

    adminAgentComponent.startMemcachedServer(
        adminAgentEntity.getIp() + ":" + adminAgentEntity.getPort(),
        ipPort.port,
        adminAgentEntity.getToken(),
        memcachedOptionsDto);

    memcachedOptionsRepository.save(MemcachedOptionsEntity.of(memcachedOptionsDto, address));
  }

  public MemcachedOptionsDto getMemcachedOptions(String address) {
    return MemcachedOptionsDto.of(
        memcachedOptionsRepository.findById(address)
            .orElseThrow(() -> new BusinessException(ApiErrorCode.AGENT_CACHE_OPTION_NOT_FOUND)));
  }

  public void stopMemcachedServer(String address) {
    IpPort ipPort = new IpPort(address);
    AdminAgentEntity adminAgentEntity = getEntityByIp(ipPort.ip);

    adminAgentComponent.stopMemcachedServer(
        adminAgentEntity.getIp() + ":" + adminAgentEntity.getPort(),
        ipPort.port,
        adminAgentEntity.getToken());
  }

  private AdminAgentEntity getEntityByIp(String ip) {
    return adminAgentRepository.findByIp(ip)
        .orElseThrow(() -> new BusinessException(ApiErrorCode.AGENT_NOT_FOUND));
  }

  private static class IpPort {

    private final String ip;

    private final int port;

    public IpPort(String address) {
      String[] splitted = address.split(":", 2);

      if (splitted.length < 2
          || StringUtils.isEmpty(splitted[0])
          || StringUtils.isEmpty((splitted[1]))
          || !NumberUtils.isDigits(splitted[1])
          || Integer.parseInt(splitted[1]) < AddressValidator.SIZE_MIN_PORT
          || Integer.parseInt(splitted[1]) > AddressValidator.SIZE_MAX_PORT) {
        throw new BusinessException(ApiErrorCode.COMMON_INVALID_PARAMETER);
      }

      ip = splitted[0];
      port = Integer.parseInt(splitted[1]);
    }
  }

}
