package com.jam2in.arcus.admin.tool.domain.cache.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CacheNodeDto {

  @Builder
  public CacheNodeDto(String address, CacheNodeStatsDto stats, boolean alive) {
    this.address = address;
    this.stats = stats;
    this.alive = alive;
  }

  @NotEmpty
  private String address;

  @Setter
  private CacheNodeStatsDto stats;

  private boolean alive;

}
