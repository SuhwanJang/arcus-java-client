package com.jam2in.arcus.admin.tool.domain.zookeeper.component;

import com.jam2in.arcus.admin.tool.domain.cluster.dto.CacheClusterDto;
import com.jam2in.arcus.admin.tool.domain.cluster.dto.ReplicationCacheClusterDto;
import com.jam2in.arcus.admin.tool.domain.zookeeper.client.ZooKeeperClient;
import com.jam2in.arcus.admin.tool.domain.zookeeper.parser.ZooKeeperZNodeParser;
import com.jam2in.arcus.admin.tool.util.PathUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

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
        CollectionUtils.emptyIfNull(
            zookeeperClient.get(connection, ARCUS_CACHE_LIST_PATH)));
  }

  @Async
  public CompletableFuture<Collection<String>> getAsyncReplicationServiceCodes(Object connection) {
    return CompletableFuture.completedFuture(
        CollectionUtils.emptyIfNull(
            zookeeperClient.get(connection, ARCUS_REPL_CACHE_LIST_PATH)));
  }

  @Async
  public CompletableFuture<Void> createAsyncServiceCode(Object connection,
                                                         CacheClusterDto clusterDto) {
    zookeeperClient.create(connection,
        ARCUS_CACHE_SERVER_LOG_PATH);

    zookeeperClient.create(connection,
        PathUtils.path(ARCUS_CLIENT_LIST_PATH, clusterDto.getServiceCode()));

    zookeeperClient.create(connection,
        PathUtils.path(ARCUS_CACHE_LIST_PATH, clusterDto.getServiceCode()));

    CollectionUtils.emptyIfNull(clusterDto.getNodes())
        .forEach(node ->
            zookeeperClient.create(connection,
                PathUtils.path(ARCUS_CACHE_SERVER_MAPPING_PATH,
                    node.getAddress(), clusterDto.getServiceCode())));

    return CompletableFuture.completedFuture(null);
  }

  @Async
  public CompletableFuture<Void> createAsyncReplicationServiceCode(
      Object connection, ReplicationCacheClusterDto replClusterDto) {
    zookeeperClient.create(connection,
        ARCUS_REPL_CACHE_SERVER_LOG_PATH);

    zookeeperClient.create(connection,
        PathUtils.path(ARCUS_REPL_CLIENT_LIST_PATH, replClusterDto.getServiceCode()));

    zookeeperClient.create(connection,
        PathUtils.path(ARCUS_REPL_CACHE_LIST_PATH, replClusterDto.getServiceCode()));

    CollectionUtils.emptyIfNull(replClusterDto.getGroups())
        .forEach(group -> {
          zookeeperClient.create(connection,
              PathUtils.path(ARCUS_REPL_GROUP_LIST_PATH,
                  replClusterDto.getServiceCode(), group.getName()));

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

    return CompletableFuture.completedFuture(null);
  }

  @Async
  public CompletableFuture<Void> deleteAsyncCacheNode(Object connection,
                                                      String cacheNodeAddress) {
    zookeeperClient.delete(connection,
        PathUtils.path(ARCUS_CACHE_SERVER_MAPPING_PATH, cacheNodeAddress));

    return CompletableFuture.completedFuture(null);
  }

  @Async
  public CompletableFuture<Void> deleteAsyncReplicationCacheNode(Object connection,
                                                                 String cacheNodeAddress) {
    zookeeperClient.delete(connection,
        PathUtils.path(ARCUS_REPL_CACHE_SERVER_MAPPING_PATH, cacheNodeAddress));

    return CompletableFuture.completedFuture(null);
  }

  @Async
  public CompletableFuture<Void> deleteAsyncServiceCode(Object connection,
                                                        String serviceCode) {
    CollectionUtils.emptyIfNull(
        zookeeperClient.get(connection,
            PathUtils.path(ARCUS_CACHE_SERVER_MAPPING_PATH)))
        .stream()
        .filter(address ->
            CollectionUtils.emptyIfNull(
                zookeeperClient.get(connection,
                    PathUtils.path(ARCUS_CACHE_SERVER_MAPPING_PATH, address)))
                .stream()
                .anyMatch(s -> StringUtils.equals(s, serviceCode)))
        .collect(Collectors.toList())
        .forEach(address ->
            zookeeperClient.delete(connection,
                PathUtils.path(ARCUS_CACHE_SERVER_MAPPING_PATH, address)));

    zookeeperClient.delete(connection,
        PathUtils.path(ARCUS_CLIENT_LIST_PATH, serviceCode));

    zookeeperClient.delete(connection,
        PathUtils.path(ARCUS_CACHE_LIST_PATH, serviceCode));

    return CompletableFuture.completedFuture(null);
  }

  @Async
  public CompletableFuture<Void> deleteAsyncReplicationServiceCode(Object connection,
                                                                   String serviceCode) {
    CollectionUtils.emptyIfNull(
        zookeeperClient.get(connection,
            PathUtils.path(ARCUS_REPL_CACHE_SERVER_MAPPING_PATH)))
        .stream()
        .filter(address ->
            CollectionUtils.emptyIfNull(
                zookeeperClient.get(connection,
                    PathUtils.path(ARCUS_REPL_CACHE_SERVER_MAPPING_PATH, address)))
                .stream()
                .anyMatch(s -> {
                  try {
                    return StringUtils.equals(
                        ZooKeeperZNodeParser.parse(s).getServiceCode(), serviceCode);
                  } catch (IllegalArgumentException e) {
                    return false;
                  }
                }))
        .collect(Collectors.toList())
        .forEach(address ->
            zookeeperClient.delete(connection,
                PathUtils.path(ARCUS_REPL_CACHE_SERVER_MAPPING_PATH, address)));

    zookeeperClient.delete(connection,
        PathUtils.path(ARCUS_REPL_CLIENT_LIST_PATH, serviceCode));

    zookeeperClient.delete(connection,
        PathUtils.path(ARCUS_REPL_GROUP_LIST_PATH, serviceCode));

    zookeeperClient.delete(connection,
        PathUtils.path(ARCUS_REPL_CACHE_LIST_PATH, serviceCode));

    return CompletableFuture.completedFuture(null);
  }

  @Async
  public CompletableFuture<Void> deleteAsyncReplicationGroup(Object connection,
                                                             String serviceCode,
                                                             String group) {
    CollectionUtils.emptyIfNull(
        zookeeperClient.get(connection, ARCUS_REPL_CACHE_SERVER_MAPPING_PATH))
        .stream()
        .filter(address ->
            CollectionUtils.emptyIfNull(
                zookeeperClient.get(connection,
                    PathUtils.path(ARCUS_REPL_CACHE_SERVER_MAPPING_PATH, address)))
            .stream()
            .anyMatch(s -> {
              try {
                ZooKeeperZNodeParser.ReplicationCacheServerMappingZNode znode
                    = ZooKeeperZNodeParser.parse(s);
                return StringUtils.equals(znode.getServiceCode(), serviceCode)
                    && StringUtils.equals(znode.getGroup(), group);
              } catch (IllegalArgumentException e) {
                return false;
              }
            }))
        .collect(Collectors.toList())
        .forEach(address ->
          zookeeperClient.delete(connection,
              PathUtils.path(ARCUS_REPL_CACHE_SERVER_MAPPING_PATH, address)));

    zookeeperClient.delete(connection,
        PathUtils.path(ARCUS_REPL_GROUP_LIST_PATH, serviceCode, group));

    return CompletableFuture.completedFuture(null);
  }

}
