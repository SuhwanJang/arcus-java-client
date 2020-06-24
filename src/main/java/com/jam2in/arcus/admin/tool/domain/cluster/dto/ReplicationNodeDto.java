package com.jam2in.arcus.admin.tool.domain.cluster.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ReplicationNodeDto {

  @Builder
  public ReplicationNodeDto(String nodeAddress, String listenAddress) {
    this.nodeAddress = nodeAddress;
    this.listenAddress = listenAddress;
  }

  @NotEmpty
  private String nodeAddress;

  @NotEmpty
  private String listenAddress;

}
