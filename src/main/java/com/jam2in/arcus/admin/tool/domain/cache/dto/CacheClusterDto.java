package com.jam2in.arcus.admin.tool.domain.cache.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CacheClusterDto extends CacheClusterBaseDto {

  @Valid
  private List<CacheNodeDto> nodes;

  @Builder
  public CacheClusterDto(String serviceCode,
                         List<CacheNodeDto> nodes) {
    super(serviceCode);
    this.nodes = nodes;
  }

}
