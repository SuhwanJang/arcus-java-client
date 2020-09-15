package com.jam2in.arcus.admin.tool.domain.memcached.component;

import com.jam2in.arcus.admin.tool.domain.memcached.dto.MemcachedNodeStatsDto;
import com.jam2in.arcus.admin.tool.domain.memcached.parser.MemcachedNodeStatsParser;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Component
public class MemcachedCommandComponent {

  // TODO: use property value
  public static final int SOCKET_TIMEOUT_MS = 10000;
  public static final int TASK_TIMEOUT_MS = 15000;

  private final MemcachedCommandAsyncComponent commandAsyncComponent;

  public MemcachedCommandComponent(MemcachedCommandAsyncComponent commandAsyncComponent) {
    this.commandAsyncComponent = commandAsyncComponent;
  }

  public CompletableFuture<MemcachedNodeStatsDto> stats(String address) {
    try {
      return commandAsyncComponent.stats(address, SOCKET_TIMEOUT_MS)
          .thenApply(stat -> MemcachedNodeStatsParser.parse(address, stat))
          .orTimeout(TASK_TIMEOUT_MS, TimeUnit.MILLISECONDS)
          .exceptionally(throwable -> MemcachedNodeStatsDto.builder()
              .throwable(throwable)
              .build());
    } catch (Exception e) {
      return CompletableFuture.completedFuture(
          MemcachedNodeStatsDto.builder()
              .throwable(e)
              .build());
    }
  }

}
