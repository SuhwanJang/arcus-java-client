package com.jam2in.arcus.admin.tool.domain.cluster.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ReplicationGroup {

  @Builder
  public ReplicationGroup(String name, ReplicationNodeDto node1, ReplicationNodeDto node2) {
    this.name = name;
    this.node1 = node1;
    this.node2 = node2;
  }

  @NotEmpty
  private String name;

  private ReplicationNodeDto node1;

  private ReplicationNodeDto node2;

}
