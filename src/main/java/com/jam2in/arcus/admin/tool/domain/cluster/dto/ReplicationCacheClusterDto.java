package com.jam2in.arcus.admin.tool.domain.cluster.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Collection;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ReplicationCacheClusterDto extends CacheClusterBaseDto {

  @Builder
  public ReplicationCacheClusterDto(String serviceCode, Collection<ReplicationGroup> groups) {
    super(serviceCode);
    this.groups = groups;
  }

  private Collection<ReplicationGroup> groups;

}
