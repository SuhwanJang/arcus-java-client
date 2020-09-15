package com.jam2in.arcus.admin.tool.domain.memcached.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MemcachedReplicationClusterDto extends MemcachedClusterBaseDto {

  @Valid
  private List<MemcachedReplicationGroupDto> groups;

  @Builder
  public MemcachedReplicationClusterDto(String serviceCode,
                                        List<MemcachedReplicationGroupDto> groups) {
    super(serviceCode);
    this.groups = groups;
  }

}
