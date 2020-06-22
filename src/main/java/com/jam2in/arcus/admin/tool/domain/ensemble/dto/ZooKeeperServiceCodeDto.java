package com.jam2in.arcus.admin.tool.domain.ensemble.dto;

import com.jam2in.arcus.admin.tool.error.ApiError;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Getter
@Slf4j
public class ZooKeeperServiceCodeDto {

  @Builder
  public ZooKeeperServiceCodeDto() {
  }

  private List<String> serviceCodes;

  private ApiError error;

}
