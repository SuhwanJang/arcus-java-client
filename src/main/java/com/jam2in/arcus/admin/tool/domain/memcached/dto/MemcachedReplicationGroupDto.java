package com.jam2in.arcus.admin.tool.domain.memcached.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MemcachedReplicationGroupDto {

  @NotEmpty
  private String group;

  @Valid
  private MemcachedReplicationNodeDto node1;

  @Valid
  private MemcachedReplicationNodeDto node2;

  @Builder
  public MemcachedReplicationGroupDto(String group,
                                      MemcachedReplicationNodeDto node1,
                                      MemcachedReplicationNodeDto node2) {
    this.group = group;
    this.node1 = node1;
    this.node2 = node2;
  }

}
