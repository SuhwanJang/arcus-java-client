package com.jam2in.arcus.admin.tool.domain.cluster.dto;

import com.jam2in.arcus.admin.tool.domain.zookeeper.util.ZooKeeperApiErrorUtil;
import com.jam2in.arcus.admin.tool.error.ApiError;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CacheNodeStatDto {

  private final String host;

  private final String port;

  private final String version;

  private final String zkTimeout;

  private final ApiError error;

  @Builder
  public CacheNodeStatDto(String host, String port, String version, String zkTimeout,
                          Throwable throwable) {
    this.host = host;
    this.port = port;
    this.version = version;
    this.zkTimeout = zkTimeout;
    this.error = ZooKeeperApiErrorUtil.toError(throwable);
  }
}
