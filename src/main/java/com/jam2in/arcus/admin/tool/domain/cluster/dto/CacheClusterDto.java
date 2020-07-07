package com.jam2in.arcus.admin.tool.domain.cluster.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Collection;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CacheClusterDto extends CacheClusterBaseDto {

  @Builder
  public CacheClusterDto(String serviceCode,
                         Collection<CacheNodeDto> nodes) {
    super(serviceCode);
    this.nodes = nodes;
  }

  private Collection<CacheNodeDto> nodes;

}
