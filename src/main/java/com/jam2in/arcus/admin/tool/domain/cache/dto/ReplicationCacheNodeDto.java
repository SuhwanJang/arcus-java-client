package com.jam2in.arcus.admin.tool.domain.cache.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ReplicationCacheNodeDto {

  @Builder
  public ReplicationCacheNodeDto(ReplicationRole role,
                                 String nodeAddress,
                                 String listenAddress,
                                 CacheNodeStatsDto stats,
                                 boolean alive) {
    this.role = role;
    this.nodeAddress = nodeAddress;
    this.listenAddress = listenAddress;
    this.stats = stats;
    this.alive = alive;
  }

  private ReplicationRole role;

  @NotEmpty
  private String nodeAddress;

  @NotEmpty
  private String listenAddress;

  @Setter
  private CacheNodeStatsDto stats;

  private boolean alive;

}
