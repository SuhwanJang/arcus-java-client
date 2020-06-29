package com.jam2in.arcus.admin.tool.domain.zookeeper.component;

import com.jam2in.arcus.admin.tool.domain.cluster.dto.CacheClusterDto;
import com.jam2in.arcus.admin.tool.domain.cluster.dto.ReplicationCacheClusterDto;
import com.jam2in.arcus.admin.tool.domain.cluster.dto.ServiceCodeDto;
import com.jam2in.arcus.admin.tool.domain.zookeeper.util.ZooKeeperApiErrorUtil;
import com.jam2in.arcus.admin.tool.exception.BusinessException;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.Functions;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.utils.CloseableUtils;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class ZooKeeperZNodeComponent {

  // TODO: use property value
  private static final int ZNODE_CONNECTION_TIMEOUT_MS = 3000;
  private static final int ZNODE_TASK_TIMEOUT_MS = 6000;

  private static final RetryPolicy RETRY_POLICY = new RetryNTimes(100, 3);

  private final ZooKeeperZNodeAsyncComponent znodeComponent;

  public ZooKeeperZNodeComponent(ZooKeeperZNodeAsyncComponent znodeComponent) {
    this.znodeComponent = znodeComponent;
  }

  public Collection<ServiceCodeDto> getServiceCodes(String addresses) {
    return handleClient(addresses,
        (client) -> znodeComponent.getAsyncNonReplServiceCodes(client)
            .thenCombine(znodeComponent.getAsyncReplServiceCodes(client),
                (nonRepl, repl) ->
                    ListUtils.union(
                        nonRepl.stream()
                            .map(serviceCode ->
                                ServiceCodeDto.builder()
                                    .serviceCode(serviceCode)
                                    .build())
                            .collect(Collectors.toList()),
                        repl.stream()
                            .map(serviceCode ->
                                ServiceCodeDto.builder()
                                    .serviceCode(serviceCode)
                                    .replication(true)
                                    .build())
                            .collect(Collectors.toList()))
            )
            .orTimeout(ZNODE_TASK_TIMEOUT_MS, TimeUnit.MILLISECONDS)
            .join());
  }

  public void createCacheCluster(String addresses, CacheClusterDto clusterDto) {
    handleClient(addresses,
        (client) -> znodeComponent.createAsyncCacheCluster(client, clusterDto)
            .orTimeout(ZNODE_TASK_TIMEOUT_MS, TimeUnit.MILLISECONDS)
            .join());
  }

  public void createReplicationCacheCluster(String addresses,
                                            ReplicationCacheClusterDto replClusterDto) {
    handleClient(addresses,
        (client) -> znodeComponent.createAsyncReplicationCacheCluster(client, replClusterDto)
            .orTimeout(ZNODE_TASK_TIMEOUT_MS, TimeUnit.MILLISECONDS)
            .join());
  }

  public void deleteCacheCluster(String addresses, CacheClusterDto clusterDto) {
    handleClient(addresses,
        (client) -> znodeComponent.deleteAsyncCacheCluster(client, clusterDto))
        .orTimeout(ZNODE_TASK_TIMEOUT_MS, TimeUnit.MILLISECONDS)
        .join();
  }

  public void deleteReplicationCacheCluster(String addresses,
                                            ReplicationCacheClusterDto replClusterDto) {
    handleClient(addresses,
        (client) -> znodeComponent.deleteAsyncReplicationCacheCluster(client, replClusterDto))
        .orTimeout(ZNODE_TASK_TIMEOUT_MS, TimeUnit.MILLISECONDS)
        .join();
  }

  public void deleteServiceCode(String addresses, String serviceCode) {
    handleClient(addresses,
        (client) -> znodeComponent.deleteAsyncServiceCode(client, serviceCode)
            .orTimeout(ZNODE_TASK_TIMEOUT_MS,  TimeUnit.SECONDS)
            .join());

  }

  public void deleteReplicationServiceCode(String addresses, String serviceCode) {
    handleClient(addresses,
        (client) -> znodeComponent.deleteAsyncReplicationServiceCode(client, serviceCode)
            .orTimeout(ZNODE_TASK_TIMEOUT_MS,  TimeUnit.SECONDS)
            .join());
  }

  private <T> T handleClient(
      String addresses,
      Functions.FailableFunction<CuratorFramework, T, Exception> function) {
    CuratorFramework client = null;

    try {
      client = znodeComponent.createAsnycClient(
          addresses, RETRY_POLICY, ZNODE_CONNECTION_TIMEOUT_MS).join();
      return function.apply(client);
    } catch (Exception e) {
      throw new BusinessException(ZooKeeperApiErrorUtil.toErrorCode(e));
    } finally {
      if (client != null) {
        CloseableUtils.closeQuietly(client);
      }
    }
  }

}
