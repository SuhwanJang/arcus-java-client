package com.jam2in.arcus.admin.tool.domain.agent.controller;

import com.jam2in.arcus.admin.tool.domain.agent.dto.AgentDto;
import com.jam2in.arcus.admin.tool.domain.agent.service.AgentService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin-agent")
public class AgentController {

  private final AgentService agentService;

  public AgentController(AgentService agentService) {
    this.agentService = agentService;
  }

  @PostMapping
  @ResponseStatus(code = HttpStatus.OK)
  public AgentDto create(@RequestBody AgentDto agentDto) {
    return agentService.create(agentDto);
  }


}
