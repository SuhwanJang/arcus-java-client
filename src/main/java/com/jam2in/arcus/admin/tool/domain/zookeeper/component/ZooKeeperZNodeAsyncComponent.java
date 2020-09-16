package com.jam2in.arcus.admin.tool.domain.zookeeper.component;

import com.jam2in.arcus.admin.tool.domain.memcached.dto.MemcachedClientDto;
import com.jam2in.arcus.admin.tool.domain.memcached.dto.MemcachedClusterDto;
import com.jam2in.arcus.admin.tool.domain.memcached.dto.MemcachedNodeDto;
import com.jam2in.arcus.admin.tool.domain.memcached.dto.MemcachedReplicationClusterDto;
import com.jam2in.arcus.admin.tool.domain.memcached.dto.MemcachedReplicationGroupDto;
import com.jam2in.arcus.admin.tool.domain.memcached.dto.MemcachedReplicationNodeDto;
import com.jam2in.arcus.admin.tool.domain.zookeeper.client.ZooKeeperClient;
import com.jam2in.arcus.admin.tool.domain.zookeeper.parser.ZooKeeperZNodeParser;
import com.jam2in.arcus.admin.tool.util.PathUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
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
  public CompletableFuture<List<String>> getAsyncServiceCodes(Object connection) {
    return CompletableFuture.completedFuture(
        ListUtils.emptyIfNull(
            zookeeperClient.get(connection, ARCUS_CACHE_LIST_PATH)));
  }

  @Async
  public CompletableFuture<List<String>> getAsyncReplicationServiceCodes(Object connection) {
    return CompletableFuture.completedFuture(
        ListUtils.emptyIfNull(
            zookeeperClient.get(connection, ARCUS_REPL_CACHE_LIST_PATH)));
  }

  @Async
  public CompletableFuture<Void> createAsyncServiceCode(Object connection,
                                                        MemcachedClusterDto clusterDto) {
    zookeeperClient.create(connection,
        ARCUS_CACHE_SERVER_LOG_PATH);

    zookeeperClient.create(connection,
        PathUtils.path(ARCUS_CLIENT_LIST_PATH, clusterDto.getServiceCode()));

    zookeeperClient.create(connection,
        PathUtils.path(ARCUS_CACHE_LIST_PATH, clusterDto.getServiceCode()));

    ListUtils.emptyIfNull(clusterDto.getNodes())
        .forEach(node ->
            zookeeperClient.create(connection,
                PathUtils.path(ARCUS_CACHE_SERVER_MAPPING_PATH,
                    node.getAddress(), clusterDto.getServiceCode())));

    return CompletableFuture.completedFuture(null);
  }

  @Async
  public CompletableFuture<Void> createAsyncReplicationServiceCode(
      Object connection,
      MemcachedReplicationClusterDto replClusterDto) {
    zookeeperClient.create(connection,
        ARCUS_REPL_CACHE_SERVER_LOG_PATH);

    zookeeperClient.create(connection,
        PathUtils.path(ARCUS_REPL_CLIENT_LIST_PATH, replClusterDto.getServiceCode()));

    zookeeperClient.create(connection,
        PathUtils.path(ARCUS_REPL_CACHE_LIST_PATH, replClusterDto.getServiceCode()));

    ListUtils.emptyIfNull(replClusterDto.getGroups())
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
    ListUtils.emptyIfNull(
        zookeeperClient.get(connection,
            PathUtils.path(ARCUS_CACHE_SERVER_MAPPING_PATH)))
        .stream()
        .filter(address ->
            ListUtils.emptyIfNull(
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
    ListUtils.emptyIfNull(
        zookeeperClient.get(connection,
            PathUtils.path(ARCUS_REPL_CACHE_SERVER_MAPPING_PATH)))
        .stream()
        .filter(address ->
            ListUtils.emptyIfNull(
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
    ListUtils.emptyIfNull(
        zookeeperClient.get(connection, ARCUS_REPL_CACHE_SERVER_MAPPING_PATH))
        .stream()
        .filter(address ->
            ListUtils.emptyIfNull(
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
  public CompletableFuture<List<MemcachedNodeDto>> getAsyncCacheNodes(Object connection,
                                                                      String serviceCode) {
    Map<String, MemcachedNodeDto> aliveCacheNodeMap = new HashMap<>();

    ListUtils.emptyIfNull(
        zookeeperClient.get(connection,
            PathUtils.path(ARCUS_CACHE_LIST_PATH, serviceCode)))
        .forEach(znode -> {
          try {
            String address = ZooKeeperZNodeParser
                .parseCacheList(znode)
                .getAddress();
            aliveCacheNodeMap.put(address,
                MemcachedNodeDto.builder()
                    .address(address)
                    .build());
          } catch (IllegalArgumentException e) {
            log.info("Failed to parse znode, {}", znode);
          }
        });

    return CompletableFuture.completedFuture(
        ListUtils.emptyIfNull(
            zookeeperClient.get(connection,
                PathUtils.path(ARCUS_CACHE_SERVER_MAPPING_PATH)))
        .stream()
        .filter(address ->
            ListUtils.emptyIfNull(
                zookeeperClient.get(connection,
                    PathUtils.path(ARCUS_CACHE_SERVER_MAPPING_PATH, address)))
                .stream()
                .anyMatch(s -> StringUtils.equals(s, serviceCode)))
        .map(address ->
          MemcachedNodeDto.builder()
              .address(address)
              .alive(aliveCacheNodeMap.containsKey(address))
              .build())
        .collect(Collectors.toList()));
  }

  @Async
  public CompletableFuture<List<MemcachedReplicationGroupDto>> getAsyncReplicationCacheNodes(
      Object connection, String serviceCode) {
    Map<String, MemcachedReplicationNodeDto> aliveCacheNodeMap = new HashMap<>();

    ListUtils.emptyIfNull(
        zookeeperClient.get(connection,
            PathUtils.path(ARCUS_REPL_CACHE_LIST_PATH, serviceCode)))
        .forEach(znode -> {
          try {
            ZooKeeperZNodeParser.ReplicationCacheListZNode parsedZnode =
                ZooKeeperZNodeParser.parseReplicationCacheList(znode);
            aliveCacheNodeMap.put(parsedZnode.getAddress(),
                MemcachedReplicationNodeDto.builder()
                    .role(parsedZnode.getRole())
                    .nodeAddress(parsedZnode.getAddress())
                    .build());
          } catch (IllegalArgumentException e) {
            log.info("Failed to parse znode, {}", znode);
          }
        });

    return CompletableFuture.completedFuture(
        ListUtils.emptyIfNull(
            zookeeperClient.get(connection,
                PathUtils.path(ARCUS_REPL_GROUP_LIST_PATH, serviceCode)))
        .stream()
        .sorted()
        .map(group -> {
          MemcachedReplicationGroupDto.MemcachedReplicationGroupDtoBuilder builder =
              MemcachedReplicationGroupDto.builder();
          builder.group(group);

          final AtomicInteger nodeCount = new AtomicInteger(0);

          ListUtils.emptyIfNull(
              zookeeperClient.get(connection, ARCUS_REPL_CACHE_SERVER_MAPPING_PATH))
              .stream()
              .sorted()
              .forEach(address ->
                ListUtils.emptyIfNull(
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
                            MemcachedReplicationNodeDto.builder()
                                .nodeAddress(address)
                                .listenAddress(parsedZnode.getListenAddress())
                                .role(aliveCacheNodeMap.containsKey(address)
                                    ? aliveCacheNodeMap.get(address).getRole() : null)
                                .alive(aliveCacheNodeMap.containsKey(address))
                                .build());
                      } else {
                        builder.node2(
                            MemcachedReplicationNodeDto.builder()
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
  public CompletableFuture<List<MemcachedClientDto>> getAsyncCacheClients(
      Object connection, String serviceCode) {
    return CompletableFuture.completedFuture(
        getCacheClients(connection, serviceCode, ARCUS_CLIENT_LIST_PATH));
  }

  @Async
  public CompletableFuture<List<MemcachedClientDto>> getAsyncReplicationCacheClients(
      Object connection, String serviceCode) {
    return CompletableFuture.completedFuture(
        getCacheClients(connection, serviceCode, ARCUS_REPL_CLIENT_LIST_PATH));
  }

  private List<MemcachedClientDto> getCacheClients(Object connection,
                                                   String serviceCode,
                                                   String path) {
    return ListUtils.emptyIfNull(
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
