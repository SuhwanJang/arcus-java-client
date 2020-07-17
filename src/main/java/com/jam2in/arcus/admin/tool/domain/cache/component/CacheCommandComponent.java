package com.jam2in.arcus.admin.tool.domain.cache.component;

import com.jam2in.arcus.admin.tool.domain.cache.dto.CacheNodeStatsDto;
import com.jam2in.arcus.admin.tool.domain.cache.parser.CacheNodeStatsParser;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Component
public class CacheCommandComponent {

  // TODO: use property value
  public static final int SOCKET_TIMEOUT_MS = 10000;
  public static final int TASK_TIMEOUT_MS = 15000;

  private final CacheCommandAsyncComponent commandAsyncComponent;

  public CacheCommandComponent(CacheCommandAsyncComponent commandAsyncComponent) {
    this.commandAsyncComponent = commandAsyncComponent;
  }

  public CompletableFuture<CacheNodeStatsDto> stats(String address) {
    try {
      return commandAsyncComponent.stats(address, SOCKET_TIMEOUT_MS)
          .thenApply(stat -> CacheNodeStatsParser.parse(address, stat))
          .orTimeout(TASK_TIMEOUT_MS, TimeUnit.MILLISECONDS)
          .exceptionally(throwable -> CacheNodeStatsDto.builder()
              .throwable(throwable)
              .build());
    } catch (Exception e) {
      return CompletableFuture.completedFuture(
          CacheNodeStatsDto.builder()
              .throwable(e)
              .build());
    }
  }

}
