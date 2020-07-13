package com.jam2in.arcus.admin.tool.domain.cluster.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CacheNodeDto {

  @Builder
  public CacheNodeDto(String address, CacheNodeStatDto stat) {
    this.address = address;
    this.stat = stat;
  }

  @NotEmpty
  private String address;

  private CacheNodeStatDto stat;

  public void setStat(CacheNodeStatDto stat) {
    this.stat = stat;
  }

}
