package com.jam2in.arcus.admin.tool.domain.cluster.dto;

import java.util.HashMap;
import java.util.Map;

public class AliveCacheNodesDto {

  private final Map<String, CacheNodeDto> map = new HashMap<>();

  public void putAliveNode(String address, CacheNodeDto cacheNodeDto) {
    map.put(address, cacheNodeDto);
  }

  public boolean isAliveNode(String address) {
    return map.containsKey(address);
  }

}
