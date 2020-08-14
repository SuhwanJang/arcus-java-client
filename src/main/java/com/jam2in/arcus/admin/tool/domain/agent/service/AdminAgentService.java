package com.jam2in.arcus.admin.tool.domain.agent.service;

import com.jam2in.arcus.admin.tool.domain.agent.dto.AdminAgentDto;
import com.jam2in.arcus.admin.tool.domain.agent.dto.MemcachedOptionsDto;
import com.jam2in.arcus.admin.tool.domain.agent.entity.AdminAgentEntity;
import com.jam2in.arcus.admin.tool.domain.agent.repository.AdminAgentRepository;
import com.jam2in.arcus.admin.tool.error.ApiErrorCode;
import com.jam2in.arcus.admin.tool.exception.BusinessException;
import lombok.Getter;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class AdminAgentService {

  private final AdminAgentRepository adminAgentRepository;

  public AdminAgentService(AdminAgentRepository adminAgentRepository) {
    this.adminAgentRepository = adminAgentRepository;
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
  }

  public void stopZooKeeperServer(String address) {
    IpPort ipPort = new IpPort(address);
    AdminAgentEntity adminAgentEntity = getEntityByIp(ipPort.ip);
  }

  public void startMemcachedServer(String address, MemcachedOptionsDto memcachedOptionsDto) {
    IpPort ipPort = new IpPort(address);
    AdminAgentEntity adminAgentEntity = getEntityByIp(ipPort.ip);
  }

  public void stopMemcachedServer(String address) {
    IpPort ipPort = new IpPort(address);
    AdminAgentEntity adminAgentEntity = getEntityByIp(ipPort.ip);
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

      ip = splitted[0];

      if (splitted.length == 2) {
        if (NumberUtils.isDigits(splitted[1])) {
          port = Integer.parseInt(splitted[1]);
        } else {
          port = 0;
        }
      } else {
        port = 0;
      }
    }

  }

}
