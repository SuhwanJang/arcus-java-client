package com.jam2in.arcus.admin.tool.domain.memcached.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MemcachedReplicationNodeDto {

  private MemcachedReplicationRole role;

  @NotEmpty
  private String nodeAddress;

  @NotEmpty
  private String listenAddress;

  @Setter
  private MemcachedNodeStatsDto stats;

  private boolean alive;

  @Builder
  public MemcachedReplicationNodeDto(MemcachedReplicationRole role,
                                     String nodeAddress,
                                     String listenAddress,
                                     MemcachedNodeStatsDto stats,
                                     boolean alive) {
    this.role = role;
    this.nodeAddress = nodeAddress;
    this.listenAddress = listenAddress;
    this.stats = stats;
    this.alive = alive;
  }

}
