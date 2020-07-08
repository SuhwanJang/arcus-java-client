package com.jam2in.arcus.admin.tool.domain.zookeeper.component;

import com.jam2in.arcus.admin.tool.domain.cluster.dto.CacheClusterDto;
import com.jam2in.arcus.admin.tool.domain.cluster.dto.ReplicationCacheClusterDto;
import com.jam2in.arcus.admin.tool.domain.zookeeper.util.ZooKeeperApiErrorUtil;
import com.jam2in.arcus.admin.tool.exception.BusinessException;
import org.apache.commons.lang3.Functions;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Component
public class ZooKeeperZNodeComponent {

  // TODO: use property value
  private static final int ZNODE_CONNECTION_TIMEOUT_MS = 3000;
  private static final int ZNODE_TASK_TIMEOUT_MS = 10000;

  private final ZooKeeperZNodeAsyncComponent znodeAsyncComponent;

  public ZooKeeperZNodeComponent(ZooKeeperZNodeAsyncComponent znodeComponent) {
    this.znodeAsyncComponent = znodeComponent;
  }

  @SuppressWarnings("unchecked")
  public Collection<String> getServiceCodes(String addresses) {
    return (Collection<String>) handleClient(addresses,
        znodeAsyncComponent::getAsyncServiceCodes);
  }

  @SuppressWarnings("unchecked")
  public Collection<String> getReplicationServiceCodes(String addresses) {
    return (Collection<String>) handleClient(addresses,
        znodeAsyncComponent::getAsyncReplicationServiceCodes);
  }

  public void createServiceCode(String addresses, CacheClusterDto clusterDto) {
    handleClient(addresses,
        connection ->
            znodeAsyncComponent.createAsyncServiceCode(connection, clusterDto));
  }

  public void createReplicationServiceCode(String addresses,
                                            ReplicationCacheClusterDto replClusterDto) {
    handleClient(addresses,
        connection ->
            znodeAsyncComponent.createAsyncReplicationServiceCode(connection, replClusterDto));
  }

  public void deleteCacheNode(String addresses, String cacheNodeAddress) {
    handleClient(addresses,
        connection ->
            znodeAsyncComponent.deleteAsyncCacheNode(connection, cacheNodeAddress));
  }

  public void deleteReplicationCacheNode(String addresses, String cacheNodeAddress) {
    handleClient(addresses,
        connection ->
            znodeAsyncComponent.deleteAsyncReplicationCacheNode(connection, cacheNodeAddress));
  }

  public void deleteServiceCode(String addresses, String serviceCode) {
    handleClient(addresses,
        connection ->
            znodeAsyncComponent.deleteAsyncServiceCode(connection, serviceCode));
  }

  public void deleteReplicationServiceCode(String addresses, String serviceCode) {
    handleClient(addresses,
        connection ->
            znodeAsyncComponent.deleteAsyncReplicationServiceCode(connection, serviceCode));
  }

  public void deleteReplicationGroup(String addresses, String serviceCode, String group) {
    handleClient(addresses,
        connection ->
            znodeAsyncComponent.deleteAsyncReplicationGroup(connection, serviceCode, group));
  }

  private Object handleClient(
      String addresses,
      Functions.FailableFunction<Object, CompletableFuture<?>, Exception> function) {
    Object connection = null;

    try {
      connection = znodeAsyncComponent.openConnection(addresses, ZNODE_CONNECTION_TIMEOUT_MS);
      return function.apply(connection)
          .orTimeout(ZNODE_TASK_TIMEOUT_MS, TimeUnit.MILLISECONDS)
          .join();
    } catch (Exception e) {
      throw new BusinessException(ZooKeeperApiErrorUtil.toErrorCode(e));
    } finally {
      znodeAsyncComponent.closeConnection(connection);
    }
  }

}
