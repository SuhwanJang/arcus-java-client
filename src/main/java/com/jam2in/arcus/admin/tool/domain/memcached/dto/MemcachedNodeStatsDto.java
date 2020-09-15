package com.jam2in.arcus.admin.tool.domain.memcached.dto;

import com.jam2in.arcus.admin.tool.domain.memcached.util.CacheApiErrorUtil;
import com.jam2in.arcus.admin.tool.error.ApiError;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemcachedNodeStatsDto {

  private final String host;

  private final String port;

  private final String version;

  private final String zkTimeout;

  private final ApiError error;

  @Builder
  public MemcachedNodeStatsDto(String host, String port, String version, String zkTimeout,
                               Throwable throwable) {
    this.host = host;
    this.port = port;
    this.version = version;
    this.zkTimeout = zkTimeout;
    this.error = CacheApiErrorUtil.toError(throwable);
  }

}
