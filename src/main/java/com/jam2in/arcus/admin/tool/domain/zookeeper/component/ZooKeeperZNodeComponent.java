package com.jam2in.arcus.admin.tool.domain.zookeeper.component;

import com.jam2in.arcus.admin.tool.domain.memcached.dto.MemcachedClientDto;
import com.jam2in.arcus.admin.tool.domain.memcached.dto.MemcachedClusterDto;
import com.jam2in.arcus.admin.tool.domain.memcached.dto.MemcachedNodeDto;
import com.jam2in.arcus.admin.tool.domain.memcached.dto.MemcachedReplicationClusterDto;
import com.jam2in.arcus.admin.tool.domain.memcached.dto.MemcachedReplicationGroupDto;
import com.jam2in.arcus.admin.tool.domain.zookeeper.util.ZooKeeperApiErrorUtils;
import com.jam2in.arcus.admin.tool.exception.BusinessException;
import org.apache.commons.lang3.Functions;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Component
public class ZooKeeperZNodeComponent {

  // TODO: use property value
  private static final int ZNODE_CONNECTION_TIMEOUT_MS = 3000;
  private static final int ZNODE_TASK_TIMEOUT_MS = 6000;

  private final ZooKeeperZNodeAsyncComponent znodeAsyncComponent;

  public ZooKeeperZNodeComponent(ZooKeeperZNodeAsyncComponent znodeComponent) {
    this.znodeAsyncComponent = znodeComponent;
  }

  public List<String> getServiceCodes(String addresses) {
    return handleClient(addresses, znodeAsyncComponent::getAsyncServiceCodes);
  }

  public List<String> getReplicationServiceCodes(String addresses) {
    return handleClient(addresses, znodeAsyncComponent::getAsyncReplicationServiceCodes);
  }

  public void createServiceCode(String addresses, MemcachedClusterDto clusterDto) {
    handleClient(addresses,
        connection ->
            znodeAsyncComponent.createAsyncServiceCode(connection, clusterDto));
  }

  public void createReplicationServiceCode(String addresses,
                                            MemcachedReplicationClusterDto replClusterDto) {
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

  public List<MemcachedNodeDto> getCacheNodes(String addresses, String serviceCode) {
    return handleClient(addresses,
        client ->
            znodeAsyncComponent.getAsyncCacheNodes(client, serviceCode));
  }

  public List<MemcachedReplicationGroupDto> getReplicationCacheNodes(String addresses,
                                                                     String serviceCode) {
    return handleClient(addresses,
        client ->
            znodeAsyncComponent.getAsyncReplicationCacheNodes(client, serviceCode));
  }

  public List<MemcachedClientDto> getCacheClients(String addresses, String serviceCode) {
    return handleClient(addresses,
        client ->
            znodeAsyncComponent.getAsyncCacheClients(client, serviceCode));
  }

  public List<MemcachedClientDto> getReplicationCacheClients(String addresses,
                                                             String serviceCode) {
    return handleClient(addresses,
        client ->
            znodeAsyncComponent.getAsyncReplicationCacheClients(client, serviceCode));
  }

  private <T> T handleClient(
      String addresses,
      Functions.FailableFunction<Object, CompletableFuture<T>, Exception> function) {
    Object connection = null;

    try {
      connection = znodeAsyncComponent.openConnection(addresses, ZNODE_CONNECTION_TIMEOUT_MS);
      return function.apply(connection)
          .orTimeout(ZNODE_TASK_TIMEOUT_MS, TimeUnit.MILLISECONDS)
          .join();
    } catch (Exception e) {
      throw new BusinessException(ZooKeeperApiErrorUtils.toError(e));
    } finally {
      znodeAsyncComponent.closeConnection(connection);
    }
  }

}
