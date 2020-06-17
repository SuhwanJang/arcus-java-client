package com.jam2in.arcus.admin.tool.domain.ensemble.controller;

import com.jam2in.arcus.admin.tool.domain.ensemble.dto.ZooKeeperDto;
import com.jam2in.arcus.admin.tool.domain.ensemble.dto.ZooKeeperFourLetterConsDto;
import com.jam2in.arcus.admin.tool.domain.ensemble.dto.ZooKeeperFourLetterSrvrDto;
import com.jam2in.arcus.admin.tool.domain.ensemble.service.ZooKeeperService;
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

  @GetMapping("/{id}")
  @ResponseStatus(code = HttpStatus.OK)
  public ZooKeeperDto get(@PathVariable long id) {
    return zookeeperService.get(id);
  }

  @GetMapping("/{id}/srvr")
  @ResponseStatus(code = HttpStatus.OK)
  public ZooKeeperFourLetterSrvrDto getSrvr(@PathVariable long id) {
    return zookeeperService.getSrvr(id);
  }

  @GetMapping("/{id}/cons")
  @ResponseStatus(code = HttpStatus.OK)
  public Collection<ZooKeeperFourLetterConsDto> getCons(@PathVariable long id) {
    return zookeeperService.getCons(id);
  }

}
