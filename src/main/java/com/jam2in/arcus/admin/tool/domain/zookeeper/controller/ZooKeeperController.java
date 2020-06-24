package com.jam2in.arcus.admin.tool.domain.zookeeper.controller;

import com.jam2in.arcus.admin.tool.domain.zookeeper.dto.ZooKeeperFourLetterConsDto;
import com.jam2in.arcus.admin.tool.domain.zookeeper.dto.ZooKeeperFourLetterMntrDto;
import com.jam2in.arcus.admin.tool.domain.zookeeper.dto.ZooKeeperFourLetterSrvrDto;
import com.jam2in.arcus.admin.tool.domain.zookeeper.service.ZooKeeperService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/api/v1/zookeepers")
public class ZooKeeperController {

  private final ZooKeeperService zookeeperService;

  public ZooKeeperController(ZooKeeperService zookeeperService) {
    this.zookeeperService = zookeeperService;
  }

  @GetMapping("/{id}/cons")
  @ResponseStatus(code = HttpStatus.OK)
  public Collection<ZooKeeperFourLetterConsDto> getCons(@PathVariable long id) {
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
