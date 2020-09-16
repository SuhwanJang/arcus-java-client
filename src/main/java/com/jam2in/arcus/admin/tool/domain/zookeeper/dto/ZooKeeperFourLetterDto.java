package com.jam2in.arcus.admin.tool.domain.zookeeper.dto;

import com.jam2in.arcus.admin.tool.domain.zookeeper.util.ZooKeeperApiErrorUtils;
import com.jam2in.arcus.admin.tool.error.ApiError;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class ZooKeeperFourLetterDto {

  private final String ruok;

  private final ZooKeeperFourLetterSrvrDto srvr;

  private final List<ZooKeeperFourLetterConsDto> cons;

  private final ApiError error;

  @Builder
  public ZooKeeperFourLetterDto(String ruok,
                                ZooKeeperFourLetterSrvrDto srvr,
                                List<ZooKeeperFourLetterConsDto> cons,
                                Throwable throwable) {
    this.ruok = ruok;
    this.srvr = srvr;
    this.cons = cons;
    this.error = ZooKeeperApiErrorUtils.toError(throwable);
  }

}
