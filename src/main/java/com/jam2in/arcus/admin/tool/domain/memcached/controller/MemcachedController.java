package com.jam2in.arcus.admin.tool.domain.memcached.controller;

import com.jam2in.arcus.admin.tool.domain.agent.dto.MemcachedOptionsDto;
import com.jam2in.arcus.admin.tool.domain.agent.service.AdminAgentService;
import com.jam2in.arcus.admin.tool.domain.common.validator.Address;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
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
@Validated
public class MemcachedController {

  private final AdminAgentService adminAgentService;

  public MemcachedController(AdminAgentService adminAgentService) {
    this.adminAgentService = adminAgentService;
  }

  @PostMapping("/{address}/install/{version}")
  @ResponseStatus(code = HttpStatus.OK)
  public void installMemcachedServer(@PathVariable("address") @Address String address,
                                     @PathVariable("version") String version) {
    adminAgentService.installMemcachedServer(address, version);
  }

  @PostMapping("/{address}/start")
  @ResponseStatus(code = HttpStatus.OK)
  public void startMemcachedServer(@PathVariable("address") @Address String address,
                                   @RequestBody @Valid MemcachedOptionsDto memcachedOptionsDto) {
    adminAgentService.startMemcachedServer(address, memcachedOptionsDto);
  }

  @PostMapping("/{address}/stop")
  @ResponseStatus(code = HttpStatus.OK)
  public void stopMemcachedServer(@PathVariable @Address String address) {
    adminAgentService.stopMemcachedServer(address);
  }

  @GetMapping("/{address}/options")
  @ResponseStatus(code = HttpStatus.OK)
  public MemcachedOptionsDto getMemcachedOptions(@PathVariable("address") @Address String address) {
    return adminAgentService.getMemcachedOptions(address);
  }

}
