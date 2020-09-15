package com.jam2in.arcus.admin.tool.domain.memcached.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MemcachedNodeDto {

  @NotEmpty
  private String address;

  @Setter
  private MemcachedNodeStatsDto stats;

  private boolean alive;

  @Builder
  public MemcachedNodeDto(String address, MemcachedNodeStatsDto stats, boolean alive) {
    this.address = address;
    this.stats = stats;
    this.alive = alive;
  }

}
