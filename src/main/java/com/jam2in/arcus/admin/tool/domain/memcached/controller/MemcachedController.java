package com.jam2in.arcus.admin.tool.domain.memcached.controller;

import com.jam2in.arcus.admin.tool.domain.agent.dto.MemcachedOptionsDto;
import com.jam2in.arcus.admin.tool.domain.agent.service.AdminAgentService;
import com.jam2in.arcus.admin.tool.domain.memcached.service.MemcachedService;
import com.jam2in.arcus.admin.tool.domain.common.validator.Address;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/mcservers")
public class MemcachedController {

  private final MemcachedService memcachedService;
  private final AdminAgentService adminAgentService;

  public MemcachedController(MemcachedService memcachedService, AdminAgentService adminAgentService) {
    this.memcachedService = memcachedService;
    this.adminAgentService = adminAgentService;
  }

  @PostMapping("/{address}/start")
  @ResponseStatus(code = HttpStatus.OK)
  public void startMemcachedServer(@PathVariable("address") @Address String address,
                                   @RequestBody @Valid MemcachedOptionsDto memcachedOptionsDto) {
    adminAgentService.startMemcachedServer(address, memcachedOptionsDto);
  }

  @GetMapping("/{address}/options")
  @ResponseStatus(code = HttpStatus.OK)
  public MemcachedOptionsDto getMemcachedOptions(@PathVariable("address") @Address String address) {
    return adminAgentService.getMemcachedOptions(address);
  }

  @PostMapping("/{address}/stop")
  @ResponseStatus(code = HttpStatus.OK)
  public void stopMemcachedServer(@PathVariable @Address String address) {
    adminAgentService.stopMemcachedServer(address);
  }

}
