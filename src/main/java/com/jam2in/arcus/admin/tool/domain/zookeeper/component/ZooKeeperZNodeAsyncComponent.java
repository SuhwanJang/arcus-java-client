package com.jam2in.arcus.admin.tool.domain.zookeeper.component;

import com.jam2in.arcus.admin.tool.domain.cluster.dto.CacheClusterDto;
import com.jam2in.arcus.admin.tool.domain.cluster.dto.ReplicationCacheClusterDto;
import com.jam2in.arcus.admin.tool.domain.zookeeper.exception.ZooKeeperException;
import com.jam2in.arcus.admin.tool.util.PathUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.utils.CloseableUtils;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.net.ConnectException;
import java.util.Collections;
import java.util.List;
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

  @Async
  public CompletableFuture<List<String>> getAsyncNonReplServiceCodes(CuratorFramework client) {
    return getZNode(client, ARCUS_CACHE_LIST_PATH);
  }

  @Async
  public CompletableFuture<List<String>> getAsyncReplServiceCodes(CuratorFramework client) {
    return getZNode(client, ARCUS_REPL_CACHE_LIST_PATH);
  }

  @Async
  public CompletableFuture<Void> createAsyncCacheCluster(CuratorFramework client,
                                                         CacheClusterDto clusterDto) {
    createZNode(client, ARCUS_CACHE_SERVER_LOG_PATH);

    createZNode(client,
        PathUtils.path(ARCUS_CLIENT_LIST_PATH,
            clusterDto.getServiceCode()));

    createZNode(client,
        PathUtils.path(ARCUS_CACHE_LIST_PATH,
            clusterDto.getServiceCode()));

    CollectionUtils.emptyIfNull(clusterDto.getAddresses()).forEach(address ->
        createZNode(client,
            PathUtils.path(ARCUS_CACHE_SERVER_MAPPING_PATH,
                address, clusterDto.getServiceCode()))
    );

    return CompletableFuture.completedFuture(null);
  }

  @Async
  public CompletableFuture<Void> createAsyncReplicationCacheCluster(
      CuratorFramework client,
      ReplicationCacheClusterDto replClusterDto) {
    createZNode(client, ARCUS_REPL_CACHE_SERVER_LOG_PATH);

    createZNode(client,
        PathUtils.path(ARCUS_REPL_CLIENT_LIST_PATH,
            replClusterDto.getServiceCode()));

    createZNode(client,
        PathUtils.path(ARCUS_REPL_CACHE_LIST_PATH,
            replClusterDto.getServiceCode()));

    replClusterDto.getGroups().forEach(group -> {
      createZNode(client,
          PathUtils.path(ARCUS_REPL_CACHE_SERVER_MAPPING_PATH,
              group.getNode1().getNodeAddress(),
              replClusterDto.getServiceCode()
                  + "^" + group.getName()
                  + "^" + group.getNode1().getListenAddress()));
      createZNode(client,
          PathUtils.path(ARCUS_REPL_CACHE_SERVER_MAPPING_PATH,
              group.getNode2().getNodeAddress(),
              replClusterDto.getServiceCode()
                  + "^" + group.getName()
                  + "^" + group.getNode2().getListenAddress()));
    });

    CollectionUtils.emptyIfNull(replClusterDto.getGroups()).forEach(group ->
        createZNode(client,
          PathUtils.path(ARCUS_REPL_GROUP_LIST_PATH,
              replClusterDto.getServiceCode(), group.getName()))
    );

    return CompletableFuture.completedFuture(null);
  }

  @Async
  public CompletableFuture<Void> deleteAsyncCacheCluster(CuratorFramework client,
                                                         CacheClusterDto clusterDto) {
    CollectionUtils.emptyIfNull(clusterDto.getAddresses()).forEach(address ->
        deleteZNode(client,
            PathUtils.path(ARCUS_CACHE_SERVER_MAPPING_PATH, address)
    ));

    return CompletableFuture.completedFuture(null);
  }

  @Async
  public CompletableFuture<Void> deleteAsyncReplicationCacheCluster(
      CuratorFramework client,
      ReplicationCacheClusterDto replClusterDto) {
    CollectionUtils.emptyIfNull(replClusterDto.getGroups()).forEach(group -> {
      if (group.getNode1() != null) {
        deleteZNode(client,
            PathUtils.path(ARCUS_REPL_CACHE_SERVER_MAPPING_PATH,
                group.getNode1().getNodeAddress()));
      }

      if (group.getNode2() != null) {
        deleteZNode(client,
            PathUtils.path(ARCUS_REPL_CACHE_SERVER_MAPPING_PATH,
                group.getNode2().getNodeAddress()));
      }
    });

    return CompletableFuture.completedFuture(null);
  }

  @Async
  public CompletableFuture<Void> deleteAsyncServiceCode(CuratorFramework client,
                                                        String serviceCode) {
    // TODO
    return CompletableFuture.completedFuture(null);
  }

  @Async
  public CompletableFuture<Void> deleteAsyncReplicationServiceCode(CuratorFramework client,
                                                                   String serviceCode) {
    // TODO
    return CompletableFuture.completedFuture(null);
  }

  @Async
  public CompletableFuture<CuratorFramework> createAsnycClient(String addresses,
                                                               RetryPolicy retryPolicy,
                                                               int connectionTimeoutMs) {
    CompletableFuture<CuratorFramework> completableFuture = new CompletableFuture<>();

    CuratorFramework client = null;

    try {
      client = CuratorFrameworkFactory.builder()
          .connectString(addresses)
          .retryPolicy(retryPolicy)
          .connectionTimeoutMs(connectionTimeoutMs)
          .build();

      client.start();

      if (!client.getZookeeperClient().blockUntilConnectedOrTimedOut()) {
        throw new ZooKeeperException(new ConnectException());
      }

      completableFuture.complete(client);
    } catch (Exception e) {
      if (client != null) {
        CloseableUtils.closeQuietly(client);
      }
      if (e instanceof ZooKeeperException) {
        throw (ZooKeeperException) e;
      }
      throw new ZooKeeperException(e);
    }

    return completableFuture;
  }

  @SuppressWarnings("unchecked")
  private CompletableFuture<List<String>> getZNode(CuratorFramework client, String path) {
    CompletableFuture<List<String>> completableFuture = new CompletableFuture<>();

    try {
      completableFuture.complete(client.getChildren().forPath(path));
    } catch (Exception e) {
      if (e instanceof KeeperException.NoNodeException) {
        completableFuture.complete(Collections.EMPTY_LIST);
      } else {
        throw new RuntimeException(e);
      }
    }

    return completableFuture;
  }

  private void createZNode(CuratorFramework client, String path) {
    try {
      client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path);
    } catch (Exception e) {
      if (!(e instanceof KeeperException.NodeExistsException)) {
        throw new RuntimeException(e);
      }
    }
  }

  private void deleteZNode(CuratorFramework client, String path) {
    try {
      client.delete().deletingChildrenIfNeeded().forPath(path);
    } catch (Exception e) {
      if (!(e instanceof KeeperException.NoNodeException)) {
        throw new RuntimeException(e);
      }
    }
  }

}
