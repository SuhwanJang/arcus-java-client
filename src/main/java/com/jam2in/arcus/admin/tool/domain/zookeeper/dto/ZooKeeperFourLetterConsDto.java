package com.jam2in.arcus.admin.tool.domain.zookeeper.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ZooKeeperFourLetterConsDto {

  private final String address;

  private final String queued;

  private final String received;

  private final String sent;

  @Builder
  public ZooKeeperFourLetterConsDto(String address,
                                    String queued,
                                    String received,
                                    String sent) {
    this.address = address;
    this.queued = queued;
    this.received = received;
    this.sent = sent;
  }

}
