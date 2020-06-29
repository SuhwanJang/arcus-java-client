package com.jam2in.arcus.admin.tool.domain.cluster.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ServiceCodeDto {

  @Builder
  public ServiceCodeDto(String serviceCode, boolean replication) {
    this.serviceCode = serviceCode;
    this.replication = replication;
  }

  private String serviceCode;

  private boolean replication;

}
