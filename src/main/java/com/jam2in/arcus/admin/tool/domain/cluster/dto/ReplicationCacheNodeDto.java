package com.jam2in.arcus.admin.tool.domain.cluster.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ReplicationCacheNodeDto {

  @Builder
  public ReplicationCacheNodeDto(String nodeAddress, String listenAddress, CacheNodeStatDto stat) {
    this.nodeAddress = nodeAddress;
    this.listenAddress = listenAddress;
    this.stat = stat;
  }

  @NotEmpty
  private String nodeAddress;

  @NotEmpty
  private String listenAddress;

  private CacheNodeStatDto stat;

  public void setStat(CacheNodeStatDto stat) {
    this.stat = stat;
  }

}
