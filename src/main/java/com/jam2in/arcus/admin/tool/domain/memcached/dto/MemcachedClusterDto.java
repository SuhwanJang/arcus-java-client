package com.jam2in.arcus.admin.tool.domain.memcached.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MemcachedClusterDto extends MemcachedClusterBaseDto {

  @Valid
  private List<MemcachedNodeDto> nodes;

  @Builder
  public MemcachedClusterDto(String serviceCode,
                             List<MemcachedNodeDto> nodes) {
    super(serviceCode);
    this.nodes = nodes;
  }

}
