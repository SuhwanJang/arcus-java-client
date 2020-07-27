package com.jam2in.arcus.admin.tool.domain.cache.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ReplicationCacheClusterDto extends CacheClusterBaseDto {

  @Valid
  private List<ReplicationCacheGroupDto> groups;

  @Builder
  public ReplicationCacheClusterDto(String serviceCode,
                                    List<ReplicationCacheGroupDto> groups) {
    super(serviceCode);
    this.groups = groups;
  }

}
