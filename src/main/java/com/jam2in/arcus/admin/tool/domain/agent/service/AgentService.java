package com.jam2in.arcus.admin.tool.domain.agent.service;

import com.jam2in.arcus.admin.tool.domain.agent.dto.AgentDto;
import com.jam2in.arcus.admin.tool.domain.agent.entity.AgentEntity;
import com.jam2in.arcus.admin.tool.domain.agent.repository.AgentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class AgentService {

  private final AgentRepository agentRepository;

  public AgentService(AgentRepository agentRepository) {
    this.agentRepository = agentRepository;
  }

  @Transactional
  public AgentDto create(AgentDto agentDto) {
    Optional<AgentEntity> optional = agentRepository.findByIp(agentDto.getIp());
    AgentEntity agentEntity;

    if (optional.isPresent()) {
      agentEntity = optional.get();
      agentEntity.updatePort(agentDto.getPort());
      agentEntity.updateToken(agentDto.getToken());
    } else {
      agentEntity = AgentEntity.of(agentDto);
    }

    return AgentDto.of(agentEntity);
  }

}
