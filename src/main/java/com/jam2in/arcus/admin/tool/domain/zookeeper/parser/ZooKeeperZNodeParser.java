package com.jam2in.arcus.admin.tool.domain.zookeeper.parser;

import com.jam2in.arcus.admin.tool.domain.cache.dto.ReplicationRole;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ZooKeeperZNodeParser {

  public static CacheListZNode parseCacheList(String znode) {
    return CacheListZNode.builder()
        .address(znode.split("\\^")[0].split("-")[0])
        .build();
  }

  public static ReplicationCacheListZNode parseReplicationCacheList(String znode) {
    String[] splitted = znode.split("\\^");
    if (splitted.length != 3) {
      throw new IllegalArgumentException();
    }

    return ReplicationCacheListZNode.builder()
        .group(splitted[0])
        .role(ReplicationRole.of(splitted[1]))
        .address(splitted[2].split("-")[0])
        .build();
  }

  public static ReplicationCacheServerMappingZNode parseReplicationCacheServerMapping(
      String znode) {
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
  public static class CacheListZNode {

    @Builder
    public CacheListZNode(String address) {
      this.address = address;
    }

    private final String address;

  }

  @Getter
  public static class ReplicationCacheListZNode {

    @Builder
    public ReplicationCacheListZNode(String group,
                                     ReplicationRole role,
                                     String address) {
      this.group = group;
      this.role = role;
      this.address = address;
    }

    private final String group;

    private final ReplicationRole role;

    private final String address;

  }

  @Getter
  public static class ReplicationCacheServerMappingZNode {

    @Builder
    public ReplicationCacheServerMappingZNode(String serviceCode,
                                              String group,
                                              String listenAddress) {
      this.serviceCode = serviceCode;
      this.group = group;
      this.listenAddress = listenAddress;
    }

    private final String serviceCode;

    private final String group;

    private final String listenAddress;

  }

}
