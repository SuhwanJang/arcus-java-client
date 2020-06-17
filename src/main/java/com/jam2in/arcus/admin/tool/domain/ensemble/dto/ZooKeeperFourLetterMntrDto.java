package com.jam2in.arcus.admin.tool.domain.ensemble.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ZooKeeperFourLetterMntrDto {

  private final String version;

  private final String averageLatency;

  private final String maxLatency;

  private final String minLatency;

  private final String packetsReceived;

  private final String packetsSent;

  private final String numAliveConnections;

  private final String outstandingRequests;

  private final String serverState;

  private final String znodeCount;

  private final String watchCount;

  private final String ephemeralsCount;

  private final String approximateDataSize;

  private final String openFileDescriptorCount;

  private final String maxFileDescriptorCount;

  @Builder
  public ZooKeeperFourLetterMntrDto(String version,
                                    String averageLatency, String maxLatency, String minLatency,
                                    String packetsReceived, String packetsSent,
                                    String numAliveConnections,
                                    String outstandingRequests,
                                    String serverState,
                                    String znodeCount,
                                    String watchCount, String ephemeralsCount,
                                    String approximateDataSize,
                                    String openFileDescriptorCount, String maxFileDescriptorCount) {
    this.version = version;
    this.averageLatency = averageLatency;
    this.maxLatency = maxLatency;
    this.minLatency = minLatency;
    this.packetsReceived = packetsReceived;
    this.packetsSent = packetsSent;
    this.numAliveConnections = numAliveConnections;
    this.outstandingRequests = outstandingRequests;
    this.serverState = serverState;
    this.znodeCount = znodeCount;
    this.watchCount = watchCount;
    this.ephemeralsCount = ephemeralsCount;
    this.approximateDataSize = approximateDataSize;
    this.openFileDescriptorCount = openFileDescriptorCount;
    this.maxFileDescriptorCount = maxFileDescriptorCount;
  }

}
