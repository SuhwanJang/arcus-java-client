package com.jam2in.arcus.admin.tool.domain.zookeeper.parser;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ZooKeeperZNodeParser {

  public static ReplicationCacheServerMappingZNode parse(String znode) {
    String[] splitted = znode.split("\\^");
    if (splitted.length != 3) {
      throw new IllegalArgumentException();
    }

    return ReplicationCacheServerMappingZNode.builder()
        .serviceCode(splitted[0])
        .group(splitted[1])
        .listenAddress(splitted[2])
        .build();
  }

  @Getter
  public static class ReplicationCacheServerMappingZNode {

    private final String serviceCode;

    private final String group;

    private final String listenAddress;

    @Builder
    public ReplicationCacheServerMappingZNode(String serviceCode,
                                              String group,
                                              String listenAddress) {
      this.serviceCode = serviceCode;
      this.group = group;
      this.listenAddress = listenAddress;
    }

  }

}
