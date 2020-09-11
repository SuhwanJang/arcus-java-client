package com.jam2in.arcus.admin.tool.domain.zookeeper.controller;

import com.jam2in.arcus.admin.tool.domain.agent.service.AdminAgentService;
import com.jam2in.arcus.admin.tool.domain.common.validator.Address;
import com.jam2in.arcus.admin.tool.domain.zookeeper.dto.ZooKeeperFourLetterConsDto;
import com.jam2in.arcus.admin.tool.domain.zookeeper.dto.ZooKeeperFourLetterMntrDto;
import com.jam2in.arcus.admin.tool.domain.zookeeper.dto.ZooKeeperFourLetterSrvrDto;
import com.jam2in.arcus.admin.tool.domain.zookeeper.service.ZooKeeperService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/zkservers")
public class ZooKeeperController {

  private final ZooKeeperService zookeeperService;
  private final AdminAgentService adminAgentService;

  public ZooKeeperController(ZooKeeperService zookeeperService,
                             AdminAgentService adminAgentService) {
    this.zookeeperService = zookeeperService;
    this.adminAgentService = adminAgentService;
  }

  @PostMapping("/{address}/start")
  @ResponseStatus(code = HttpStatus.OK)
  public void startZooKeeperServer(@PathVariable @Address String address) {
    adminAgentService.startZooKeeperServer(address);
  }

  @PostMapping("/{address}/stop")
  @ResponseStatus(code = HttpStatus.OK)
  public void stopZooKeeperServer(@PathVariable @Address String address) {
    adminAgentService.stopZooKeeperServer(address);
  }

  @GetMapping("/{id}/cons")
  @ResponseStatus(code = HttpStatus.OK)
  public List<ZooKeeperFourLetterConsDto> getCons(@PathVariable long id) {
    return zookeeperService.getCons(id);
  }

  @GetMapping("/{id}/srvr")
  @ResponseStatus(code = HttpStatus.OK)
  public ZooKeeperFourLetterSrvrDto getSrvr(@PathVariable long id) {
    return zookeeperService.getSrvr(id);
  }

  @GetMapping("/{id}/mntr")
  @ResponseStatus(code = HttpStatus.OK)
  public ZooKeeperFourLetterMntrDto getMntr(@PathVariable long id) {
    return zookeeperService.getMntr(id);
  }

}
