package com.jam2in.arcus.admin.tool.domain.cache.dto;

import java.util.HashMap;
import java.util.Map;

public class AliveReplicationCacheNodesDto {

  private final Map<String, ReplicationCacheNodeDto> map = new HashMap<>();

  public void putAliveNode(String address, ReplicationCacheNodeDto cacheNodeDto) {
    map.put(address, cacheNodeDto);
  }

  public boolean isAliveNode(String address) {
    return map.containsKey(address);
  }

  public ReplicationRole getRole(String address) {
    ReplicationCacheNodeDto cacheNodeDto = map.get(address);
    return cacheNodeDto != null ? cacheNodeDto.getRole() : null;
  }

}
