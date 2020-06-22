package com.jam2in.arcus.admin.tool.domain.ensemble.dto;

import com.jam2in.arcus.admin.tool.domain.ensemble.util.ZooKeeperApiErrorUtil;
import com.jam2in.arcus.admin.tool.error.ApiError;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;

@Getter
@Slf4j
public class ZooKeeperFourLetterDto {

  private final String ruok;

  private final ZooKeeperFourLetterSrvrDto srvr;

  private final Collection<ZooKeeperFourLetterConsDto> cons;

  private final ApiError error;

  @Builder
  public ZooKeeperFourLetterDto(String ruok,
                                ZooKeeperFourLetterSrvrDto srvr,
                                Collection<ZooKeeperFourLetterConsDto> cons,
                                Throwable throwable) {
    this.ruok = ruok;
    this.srvr = srvr;
    this.cons = cons;
    this.error = ZooKeeperApiErrorUtil.toError(throwable);
  }

}
