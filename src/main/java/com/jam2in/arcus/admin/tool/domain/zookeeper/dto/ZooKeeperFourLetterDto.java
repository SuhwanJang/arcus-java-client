package com.jam2in.arcus.admin.tool.domain.zookeeper.dto;

import com.jam2in.arcus.admin.tool.domain.zookeeper.util.ZooKeeperApiErrorUtil;
import com.jam2in.arcus.admin.tool.error.ApiError;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class ZooKeeperFourLetterDto {

  @Builder
  public ZooKeeperFourLetterDto(String ruok,
                                ZooKeeperFourLetterSrvrDto srvr,
                                List<ZooKeeperFourLetterConsDto> cons,
                                Throwable throwable) {
    this.ruok = ruok;
    this.srvr = srvr;
    this.cons = cons;
    this.error = ZooKeeperApiErrorUtil.toError(throwable);
  }

  private final String ruok;

  private final ZooKeeperFourLetterSrvrDto srvr;

  private final List<ZooKeeperFourLetterConsDto> cons;

  private final ApiError error;

}
