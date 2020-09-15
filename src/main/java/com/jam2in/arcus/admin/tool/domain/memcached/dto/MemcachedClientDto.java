package com.jam2in.arcus.admin.tool.domain.memcached.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MemcachedClientDto {

  private final String host;

  private final String address;

  private final String poolSize;

  private final String client;

  private final String version;

  private final String date;

  private final String sessionId;

  @Builder
  public MemcachedClientDto(String host,
                            String address,
                            String poolSize,
                            String client,
                            String version,
                            String date,
                            String sessionId) {
    this.host = host;
    this.address = address;
    this.poolSize = poolSize;
    this.client = client;
    this.version = version;
    this.date = date;
    this.sessionId = sessionId;
  }

}
