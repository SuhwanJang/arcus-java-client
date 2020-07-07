package com.jam2in.arcus.admin.tool.domain.zookeeper.component;

import com.jam2in.arcus.admin.tool.domain.cluster.dto.CacheClusterDto;
import com.jam2in.arcus.admin.tool.domain.cluster.dto.ReplicationCacheClusterDto;
import com.jam2in.arcus.admin.tool.domain.zookeeper.client.ZooKeeperClient;
import com.jam2in.arcus.admin.tool.util.PathUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

@Component
public class ZooKeeperZNodeAsyncComponent {

  private static final String ARCUS_CACHE_SERVER_LOG_PATH =
      "/arcus/cache_server_log";
  private static final String ARCUS_CLIENT_LIST_PATH =
      "/arcus/client_list";
  private static final String ARCUS_CACHE_LIST_PATH =
      "/arcus/cache_list";
  private static final String ARCUS_CACHE_SERVER_MAPPING_PATH =
      "/arcus/cache_server_mapping";

  private static final String ARCUS_REPL_CACHE_SERVER_LOG_PATH =
      "/arcus_repl/cache_server_log";
  private static final String ARCUS_REPL_CLIENT_LIST_PATH =
      "/arcus_repl/client_list";
  private static final String ARCUS_REPL_CACHE_LIST_PATH =
      "/arcus_repl/cache_list";
  private static final String ARCUS_REPL_CACHE_SERVER_MAPPING_PATH =
      "/arcus_repl/cache_server_mapping";
  private static final String ARCUS_REPL_GROUP_LIST_PATH =
      "/arcus_repl/group_list";

  private final ZooKeeperClient zookeeperClient;

  public ZooKeeperZNodeAsyncComponent(ZooKeeperClient zookeeperClient) {
    this.zookeeperClient = zookeeperClient;
  }

  public Object openConnection(String address, int connectionTimeoutMs) {
    return zookeeperClient.open(address, connectionTimeoutMs);
  }

  public void closeConnection(Object connection) {
    zookeeperClient.close(connection);
  }

  @Async
  public CompletableFuture<Collection<String>> getAsyncServiceCodes(Object connection) {
    return CompletableFuture.completedFuture(
        zookeeperClient.get(connection, ARCUS_CACHE_LIST_PATH));
  }

  @Async
  public CompletableFuture<Collection<String>> getAsyncReplicationServiceCodes(Object connection) {
    return CompletableFuture.completedFuture(
        zookeeperClient.get(connection, ARCUS_REPL_CACHE_LIST_PATH));
  }

  @Async
  public CompletableFuture<Void> createAsyncCacheCluster(Object connection,
                                                         CacheClusterDto clusterDto) {
    zookeeperClient.create(connection,
        ARCUS_CACHE_SERVER_LOG_PATH);

    zookeeperClient.create(connection,
        PathUtils.path(ARCUS_CLIENT_LIST_PATH, clusterDto.getServiceCode()));

    zookeeperClient.create(connection,
        PathUtils.path(ARCUS_CACHE_LIST_PATH, clusterDto.getServiceCode()));

    CollectionUtils.emptyIfNull(clusterDto.getNodes()).forEach(node ->
        zookeeperClient.create(connection,
            PathUtils.path(ARCUS_CACHE_SERVER_MAPPING_PATH,
                node.getAddress(), clusterDto.getServiceCode())));

    return CompletableFuture.completedFuture(null);
  }

  @Async
  public CompletableFuture<Void> createAsyncReplicationCacheCluster(
      Object connection, ReplicationCacheClusterDto replClusterDto) {
    zookeeperClient.create(connection,
        ARCUS_REPL_CACHE_SERVER_LOG_PATH);

    zookeeperClient.create(connection,
        PathUtils.path(ARCUS_REPL_CLIENT_LIST_PATH, replClusterDto.getServiceCode()));

    zookeeperClient.create(connection,
        PathUtils.path(ARCUS_REPL_CACHE_LIST_PATH, replClusterDto.getServiceCode()));

    replClusterDto.getGroups().forEach(group -> {
      zookeeperClient.create(connection,
          PathUtils.path(ARCUS_REPL_CACHE_SERVER_MAPPING_PATH,
              group.getNode1().getNodeAddress(),
              replClusterDto.getServiceCode()
                  + "^" + group.getName()
                  + "^" + group.getNode1().getListenAddress()));

      zookeeperClient.create(connection,
          PathUtils.path(ARCUS_REPL_CACHE_SERVER_MAPPING_PATH,
              group.getNode2().getNodeAddress(),
              replClusterDto.getServiceCode()
                  + "^" + group.getName()
                  + "^" + group.getNode2().getListenAddress()));
    });

    CollectionUtils.emptyIfNull(replClusterDto.getGroups()).forEach(group ->
        zookeeperClient.create(connection,
            PathUtils.path(ARCUS_REPL_GROUP_LIST_PATH,
              replClusterDto.getServiceCode(), group.getName())));

    return CompletableFuture.completedFuture(null);
  }

  @Async
  public CompletableFuture<Void> deleteCacheNode(Object connection,
                                                 String cacheNodeAddress) {
    zookeeperClient.delete(connection,
        PathUtils.path(ARCUS_CACHE_SERVER_MAPPING_PATH, cacheNodeAddress));

    return CompletableFuture.completedFuture(null);
  }

  @Async
  public CompletableFuture<Void> deleteReplicationCacheNode(Object connection,
                                                            String cacheNodeAddress) {
    zookeeperClient.delete(connection,
        PathUtils.path(ARCUS_REPL_CACHE_SERVER_MAPPING_PATH, cacheNodeAddress));

    return CompletableFuture.completedFuture(null);
  }

  @Async
  public CompletableFuture<Void> deleteAsyncCacheCluster(Object connection,
                                                         CacheClusterDto clusterDto) {
    CollectionUtils.emptyIfNull(clusterDto.getNodes()).forEach(node ->
        zookeeperClient.delete(connection,
            PathUtils.path(ARCUS_CACHE_SERVER_MAPPING_PATH, node.getAddress())));

    return CompletableFuture.completedFuture(null);
  }

  @Async
  public CompletableFuture<Void> deleteAsyncReplicationCacheCluster(Object connection,
                                                                    CacheClusterDto clusterDto) {
    CollectionUtils.emptyIfNull(clusterDto.getNodes()).forEach(node ->
        zookeeperClient.delete(connection,
            PathUtils.path(ARCUS_CACHE_SERVER_MAPPING_PATH, node.getAddress())));

    return CompletableFuture.completedFuture(null);
  }

  @Async
  public CompletableFuture<Void> deleteAsyncServiceCode(Object connection,
                                                        String serviceCode) {
    // TODO
    return CompletableFuture.completedFuture(null);
  }

  @Async
  public CompletableFuture<Void> deleteAsyncReplicationServiceCode(Object connection,
                                                                   String serviceCode) {
    // TODO
    return CompletableFuture.completedFuture(null);
  }

}
