package com.jam2in.arcus.admin.tool.domain.cluster.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ReplicationCacheGroupDto {

  @Builder
  public ReplicationCacheGroupDto(String group,
                                  ReplicationCacheNodeDto node1,
                                  ReplicationCacheNodeDto node2) {
    this.group = group;
    this.node1 = node1;
    this.node2 = node2;
  }

  @NotEmpty
  private String group;

  private ReplicationCacheNodeDto node1;

  private ReplicationCacheNodeDto node2;

}
