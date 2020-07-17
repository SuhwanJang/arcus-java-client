package com.jam2in.arcus.admin.tool.domain.zookeeper.component;

import com.jam2in.arcus.admin.tool.domain.cache.dto.CacheClientsDto;
import com.jam2in.arcus.admin.tool.domain.cache.dto.CacheClusterDto;
import com.jam2in.arcus.admin.tool.domain.cache.dto.CacheNodeDto;
import com.jam2in.arcus.admin.tool.domain.cache.dto.ReplicationCacheClusterDto;
import com.jam2in.arcus.admin.tool.domain.cache.dto.ReplicationCacheGroupDto;
import com.jam2in.arcus.admin.tool.domain.cache.dto.ReplicationCacheNodeDto;
import com.jam2in.arcus.admin.tool.domain.zookeeper.client.ZooKeeperClient;
import com.jam2in.arcus.admin.tool.domain.zookeeper.parser.ZooKeeperZNodeParser;
import com.jam2in.arcus.admin.tool.util.PathUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ZooKeeperZNodeAsyncComponent {

  private static final int MAX_REPLICATION_NODE_COUNT_PER_GROUP = 2;

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
      Object connection,
      ReplicationCacheClusterDto replClusterDto) {
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
                  replClusterDto.getServiceCode(), group.getGroup()));

          if (group.getNode1() != null) {
            zookeeperClient.create(connection,
                PathUtils.path(ARCUS_REPL_CACHE_SERVER_MAPPING_PATH,
                    group.getNode1().getNodeAddress(),
                    replClusterDto.getServiceCode()
                        + "^" + group.getGroup()
                        + "^" + group.getNode1().getListenAddress()));
          }

          if (group.getNode2() != null) {
            zookeeperClient.create(connection,
                PathUtils.path(ARCUS_REPL_CACHE_SERVER_MAPPING_PATH,
                    group.getNode2().getNodeAddress(),
                    replClusterDto.getServiceCode()
                        + "^" + group.getGroup()
                        + "^" + group.getNode2().getListenAddress()));
          }
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
                        ZooKeeperZNodeParser
                            .parseReplicationCacheServerMapping(s)
                            .getServiceCode(), serviceCode);
                  } catch (IllegalArgumentException e) {
                    log.info("Failed to parse znode, {}", address);
                    return false;
                  }
                }))
        .collect(Collectors.toList())
        .forEach(address ->
            zookeeperClient.delete(connection,
                PathUtils.path(ARCUS_REPL_CACHE_SERVER_MAPPING_PATH, address)));

    zookeeperClient.delete(connection,
        PathUtils.path(ARCUS_REPL_GROUP_LIST_PATH, serviceCode));

    zookeeperClient.delete(connection,
        PathUtils.path(ARCUS_REPL_CLIENT_LIST_PATH, serviceCode));

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
                    = ZooKeeperZNodeParser.parseReplicationCacheServerMapping(s);
                return StringUtils.equals(znode.getServiceCode(), serviceCode)
                    && StringUtils.equals(znode.getGroup(), group);
              } catch (IllegalArgumentException e) {
                log.info(e.getMessage(), e);
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

  @Async
  public CompletableFuture<Collection<CacheNodeDto>> getAsyncCacheNodes(Object connection,
                                                                        String serviceCode) {
    Map<String, CacheNodeDto> aliveCacheNodeMap = new HashMap<>();

    CollectionUtils.emptyIfNull(
        zookeeperClient.get(connection,
            PathUtils.path(ARCUS_CACHE_LIST_PATH, serviceCode)))
        .forEach(znode -> {
          try {
            String address = ZooKeeperZNodeParser
                .parseCacheList(znode)
                .getAddress();
            aliveCacheNodeMap.put(address,
                CacheNodeDto.builder()
                    .address(address)
                    .build());
          } catch (IllegalArgumentException e) {
            log.info("Failed to parse znode, {}", znode);
          }
        });

    return CompletableFuture.completedFuture(
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
        .map(address ->
          CacheNodeDto.builder()
              .address(address)
              .alive(aliveCacheNodeMap.containsKey(address))
              .build())
        .collect(Collectors.toList()));
  }

  @Async
  public CompletableFuture<Collection<ReplicationCacheGroupDto>> getAsyncReplicationCacheNodes(
      Object connection, String serviceCode) {
    Map<String, ReplicationCacheNodeDto> aliveCacheNodeMap = new HashMap<>();

    CollectionUtils.emptyIfNull(
        zookeeperClient.get(connection,
            PathUtils.path(ARCUS_REPL_CACHE_LIST_PATH, serviceCode)))
        .forEach(znode -> {
          try {
            ZooKeeperZNodeParser.ReplicationCacheListZNode parsedZnode =
                ZooKeeperZNodeParser.parseReplicationCacheList(znode);
            aliveCacheNodeMap.put(parsedZnode.getAddress(),
                ReplicationCacheNodeDto.builder()
                    .role(parsedZnode.getRole())
                    .nodeAddress(parsedZnode.getAddress())
                    .build());
          } catch (IllegalArgumentException e) {
            log.info("Failed to parse znode, {}", znode);
          }
        });

    return CompletableFuture.completedFuture(
        CollectionUtils.emptyIfNull(
            zookeeperClient.get(connection,
                PathUtils.path(ARCUS_REPL_GROUP_LIST_PATH, serviceCode)))
        .stream()
        .sorted()
        .map(group -> {
          ReplicationCacheGroupDto.ReplicationCacheGroupDtoBuilder builder =
              ReplicationCacheGroupDto.builder();
          builder.group(group);

          final AtomicInteger nodeCount = new AtomicInteger(0);

          CollectionUtils.emptyIfNull(
              zookeeperClient.get(connection, ARCUS_REPL_CACHE_SERVER_MAPPING_PATH))
              .stream()
              .sorted()
              .forEach(address ->
                CollectionUtils.emptyIfNull(
                    zookeeperClient.get(connection,
                        PathUtils.path(ARCUS_REPL_CACHE_SERVER_MAPPING_PATH, address)))
                .stream()
                .takeWhile(znode -> nodeCount.get() != MAX_REPLICATION_NODE_COUNT_PER_GROUP)
                .forEach(znode -> {
                  try {
                    ZooKeeperZNodeParser.ReplicationCacheServerMappingZNode parsedZnode
                        = ZooKeeperZNodeParser.parseReplicationCacheServerMapping(znode);
                    if (StringUtils.equals(parsedZnode.getServiceCode(), serviceCode)
                        && StringUtils.equals(parsedZnode.getGroup(), group)) {
                      if (nodeCount.getAndIncrement() == 0) {
                        builder.node1(
                            ReplicationCacheNodeDto.builder()
                                .nodeAddress(address)
                                .listenAddress(parsedZnode.getListenAddress())
                                .role(aliveCacheNodeMap.containsKey(address)
                                    ? aliveCacheNodeMap.get(address).getRole() : null)
                                .alive(aliveCacheNodeMap.containsKey(address))
                                .build());
                      } else {
                        builder.node2(
                            ReplicationCacheNodeDto.builder()
                                .nodeAddress(address)
                                .listenAddress(parsedZnode.getListenAddress())
                                .listenAddress(parsedZnode.getListenAddress())
                                .role(aliveCacheNodeMap.containsKey(address)
                                    ? aliveCacheNodeMap.get(address).getRole() : null)
                                .alive(aliveCacheNodeMap.containsKey(address))
                                .build());
                      }
                    }
                  } catch (IllegalArgumentException e) {
                    log.info("Failed to parse znode, {}", znode);
                  }
                }));
          return builder.build();
        }).collect(Collectors.toList()));
  }

  @Async
  public CompletableFuture<Collection<CacheClientsDto>> getAsyncCacheClients(
      Object connection, String serviceCode) {
    return CompletableFuture.completedFuture(
        getCacheClients(connection, serviceCode, ARCUS_CLIENT_LIST_PATH));
  }

  @Async
  public CompletableFuture<Collection<CacheClientsDto>> getAsyncReplicationCacheClients(
      Object connection, String serviceCode) {
    return CompletableFuture.completedFuture(
        getCacheClients(connection, serviceCode, ARCUS_REPL_CLIENT_LIST_PATH));
  }

  private Collection<CacheClientsDto> getCacheClients(Object connection,
                                                      String serviceCode,
                                                      String path) {
    return CollectionUtils.emptyIfNull(
        zookeeperClient.get(connection,
            PathUtils.path(path, serviceCode)))
        .stream()
        .map(znode -> {
          try {
            return ZooKeeperZNodeParser.parseCacheClientList(znode);
          } catch (IllegalArgumentException e) {
            log.info("Failed to parse znode, {}", znode);
            return null;
          }
        })
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
  }

}
