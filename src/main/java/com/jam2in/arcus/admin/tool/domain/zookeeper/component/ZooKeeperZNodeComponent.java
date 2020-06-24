package com.jam2in.arcus.admin.tool.domain.zookeeper.component;

import com.jam2in.arcus.admin.tool.domain.cluster.dto.CacheClusterDto;
import com.jam2in.arcus.admin.tool.domain.cluster.dto.ReplicationCacheClusterDto;
import com.jam2in.arcus.admin.tool.domain.zookeeper.util.ZooKeeperApiErrorUtil;
import com.jam2in.arcus.admin.tool.exception.BusinessException;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.Functions;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.utils.CloseableUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

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

  public List<String> getServiceCodes(String addresses) {
    return handleClient(addresses,
        (client) -> znodeComponent.getAsyncNonReplServiceCodes(client)
            .thenCombine(znodeComponent.getAsyncReplServiceCodes(client), ListUtils::union)
            .orTimeout(ZNODE_TASK_TIMEOUT_MS, TimeUnit.MILLISECONDS)
            .join());
  }

  public void createCacheCluster(String addresses,
                                 CacheClusterDto clusterDto) {
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
