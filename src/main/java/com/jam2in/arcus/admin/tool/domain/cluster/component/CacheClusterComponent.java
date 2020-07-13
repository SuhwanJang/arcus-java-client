package com.jam2in.arcus.admin.tool.domain.cluster.component;

import com.jam2in.arcus.admin.tool.domain.cluster.dto.CacheNodeDto;
import com.jam2in.arcus.admin.tool.domain.cluster.dto.CacheNodeStatDto;
import com.jam2in.arcus.admin.tool.domain.cluster.dto.ReplicationCacheGroupDto;
import com.jam2in.arcus.admin.tool.domain.cluster.dto.ReplicationCacheNodeDto;
import com.jam2in.arcus.admin.tool.domain.cluster.parser.CacheNodeStatParser;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class CacheClusterComponent {

  // TODO: use property value
  public static final int SOCKET_TIMEOUT_MS = 10000;
  public static final int TASK_TIMEOUT_MS = 15000;

  private final CacheClusterAsyncComponent cacheClusterAsyncComponent;

  public CacheClusterComponent(CacheClusterAsyncComponent cacheClusterAsyncComponent) {
    this.cacheClusterAsyncComponent = cacheClusterAsyncComponent;
  }

  public Collection<CacheNodeDto> getAllStats(Collection<CacheNodeDto> cacheNodes) {
    return cacheNodes.stream()
        .map(this::getStat)
        .collect(Collectors.toList())
        .stream()
        .map(CompletableFuture::join)
        .collect(Collectors.toList());
  }

  public Collection<ReplicationCacheGroupDto> getReplicationAllStats(
      Collection<ReplicationCacheGroupDto> cacheNodes) {
    return cacheNodes.stream()
        .map(group -> {
          Stream.of(getReplicationStat(group.getNode1()), getReplicationStat(group.getNode2()))
              .filter(x -> x != null)
              .collect(Collectors.toList())
              .stream()
              .map(future -> {
                if (group.getNode1().getStat() == null) {
                  future.thenAccept(stat -> group.getNode1().setStat(stat));
                } else {
                  future.thenAccept(stat -> group.getNode2().setStat(stat));
                }
                return future;
              })
              .collect(Collectors.toList())
              .stream().map(CompletableFuture::join);
          return group;
        })
        .collect(Collectors.toList());
  }

  private CompletableFuture<CacheNodeDto> getStat(CacheNodeDto cacheNodeDto) {
    try {
      return cacheClusterAsyncComponent.stat(cacheNodeDto.getAddress(), SOCKET_TIMEOUT_MS)
          .thenApply(stat -> {
            cacheNodeDto.setStat(CacheNodeStatParser.parse(cacheNodeDto.getAddress(), stat));
            return cacheNodeDto;
          })
          .orTimeout(TASK_TIMEOUT_MS, TimeUnit.MILLISECONDS)
          .exceptionally(throwable -> {
            cacheNodeDto.setStat(CacheNodeStatDto.builder().throwable(throwable).build());
            return cacheNodeDto;
          });
    } catch (Exception e) {
      cacheNodeDto.setStat(CacheNodeStatDto.builder().throwable(e).build());
      return CompletableFuture.completedFuture(cacheNodeDto);
    }
  }

  private CompletableFuture<CacheNodeStatDto> getReplicationStat(
      ReplicationCacheNodeDto cacheNodeDto) {
    if (cacheNodeDto == null) {
      return null;
    }
    try {
      return cacheClusterAsyncComponent.stat(cacheNodeDto.getNodeAddress(), SOCKET_TIMEOUT_MS)
          .thenApply(stat -> CacheNodeStatParser.parse(cacheNodeDto.getNodeAddress(), stat))
          .orTimeout(TASK_TIMEOUT_MS, TimeUnit.MILLISECONDS)
          .exceptionally(throwable -> CacheNodeStatDto.builder().throwable(throwable).build());
    } catch (Exception e) {
      return CompletableFuture.completedFuture(CacheNodeStatDto.builder().throwable(e).build());
    }
  }
}
