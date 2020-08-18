package com.jam2in.arcus.admin.tool.domain.agent.controller;

import com.jam2in.arcus.admin.tool.domain.agent.dto.AdminAgentDto;
import com.jam2in.arcus.admin.tool.domain.agent.dto.MemcachedOptionsDto;
import com.jam2in.arcus.admin.tool.domain.agent.service.AdminAgentService;
import com.jam2in.arcus.admin.tool.domain.common.validator.Address;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/admin-agent")
@Validated
public class AdminAgentController {

  private final AdminAgentService adminAgentService;

  public AdminAgentController(AdminAgentService agentService) {
    this.adminAgentService = agentService;
  }

  @PostMapping
  @ResponseStatus(code = HttpStatus.OK)
  public AdminAgentDto create(@RequestBody @Valid AdminAgentDto adminAgentDto) {
    return adminAgentService.create(adminAgentDto);
  }

  @PostMapping("/zkservers/{address}/start")
  @ResponseStatus(code = HttpStatus.OK)
  public void startZooKeeperServer(@PathVariable @Address String address) {
    adminAgentService.startZooKeeperServer(address);
  }

  @PostMapping("/zkservers/{address}/stop")
  @ResponseStatus(code = HttpStatus.OK)
  public void stopZooKeeperServer(@PathVariable @Address String address) {
    adminAgentService.stopZooKeeperServer(address);
  }

  @PostMapping("/mcservers/{address}/start")
  @ResponseStatus(code = HttpStatus.OK)
  public void startMemcachedServer(@PathVariable("address") @Address String address,
                                   @RequestBody @Valid MemcachedOptionsDto memcachedOptionsDto) {
    adminAgentService.startMemcachedServer(address, memcachedOptionsDto);
  }

  @PostMapping("/mcservers/{address}/stop")
  @ResponseStatus(code = HttpStatus.OK)
  public void stopMemcachedServer(@PathVariable @Address String address) {
    adminAgentService.stopMemcachedServer(address);
  }

}
