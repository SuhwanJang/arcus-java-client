package com.jam2in.arcus.admin.tool.domain.zookeeper.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ZooKeeperFourLetterSrvrDto {

  private final String version;

  private final String latencyMin;

  private final String latencyAvg;

  private final String latencyMax;

  private final String received;

  private final String sent;

  private final String connections;

  private final String outstanding;

  private final String zxId;

  private final String mode;

  private final String nodeCount;

  @Builder
  public ZooKeeperFourLetterSrvrDto(String version,
                                    String latencyMin, String latencyAvg, String latencyMax,
                                    String received, String sent,
                                    String connections, String outstanding,
                                    String zxId, String mode, String nodeCount) {
    this.version = version;
    this.latencyMin = latencyMin;
    this.latencyAvg = latencyAvg;
    this.latencyMax = latencyMax;
    this.received = received;
    this.sent = sent;
    this.connections = connections;
    this.outstanding = outstanding;
    this.zxId = zxId;
    this.mode = mode;
    this.nodeCount = nodeCount;
  }

}
