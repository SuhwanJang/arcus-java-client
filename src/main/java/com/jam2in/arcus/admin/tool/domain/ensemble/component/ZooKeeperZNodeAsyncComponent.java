package com.jam2in.arcus.admin.tool.domain.ensemble.component;

import com.jam2in.arcus.admin.tool.domain.ensemble.exception.ZooKeeperException;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.utils.CloseableUtils;
import org.apache.zookeeper.KeeperException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.net.ConnectException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
public class ZooKeeperZNodeAsyncComponent {

  @SuppressWarnings("unchecked")
  private CompletableFuture<List<String>> getChildrens(CuratorFramework client,
                                                      String path) {
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

  @Async
  public CompletableFuture<List<String>> getNonReplServiceCodes(CuratorFramework client) {
    return getChildrens(client, "/arcus/cache_list");
  }

  @Async
  public CompletableFuture<List<String>> getReplServiceCodes(CuratorFramework client) {
    return getChildrens(client, "/arcus_repl/cache_list");
  }

  @Async
  public CompletableFuture<CuratorFramework> createClient(String addresses,
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

}
