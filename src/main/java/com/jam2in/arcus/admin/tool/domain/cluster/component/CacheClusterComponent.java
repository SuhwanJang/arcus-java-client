package com.jam2in.arcus.admin.tool.domain.cluster.component;

import com.jam2in.arcus.admin.tool.domain.cluster.dto.CacheNodeStatsDto;
import com.jam2in.arcus.admin.tool.domain.cluster.parser.CacheNodeStatsParser;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Component
public class CacheClusterComponent {

  // TODO: use property value
  public static final int SOCKET_TIMEOUT_MS = 10000;
  public static final int TASK_TIMEOUT_MS = 15000;

  private final CacheClusterAsyncComponent cacheClusterAsyncComponent;

  public CacheClusterComponent(CacheClusterAsyncComponent cacheClusterAsyncComponent) {
    this.cacheClusterAsyncComponent = cacheClusterAsyncComponent;
  }

  public CompletableFuture<CacheNodeStatsDto> getStats(String address) {
    try {
      return cacheClusterAsyncComponent.stats(address, SOCKET_TIMEOUT_MS)
          .thenApply(stat -> CacheNodeStatsParser.parse(address, stat))
          .orTimeout(TASK_TIMEOUT_MS, TimeUnit.MILLISECONDS)
          .exceptionally(throwable -> CacheNodeStatsDto.builder().throwable(throwable).build());
    } catch (Exception e) {
      return CompletableFuture.completedFuture(
          CacheNodeStatsDto.builder().throwable(e).build());
    }
  }

}
