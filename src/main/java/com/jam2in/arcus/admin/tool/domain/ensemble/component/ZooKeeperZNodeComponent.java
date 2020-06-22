package com.jam2in.arcus.admin.tool.domain.ensemble.component;

import com.jam2in.arcus.admin.tool.domain.ensemble.util.ZooKeeperApiErrorUtil;
import com.jam2in.arcus.admin.tool.exception.BusinessException;
import org.apache.commons.collections4.ListUtils;
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
  private static final int ZNODE_TASK_TIMEOUT_MS = 3000;

  private static final RetryPolicy RETRY_POLICY = new RetryNTimes(100, 3);

  private final ZooKeeperZNodeAsyncComponent znodeComponent;

  public ZooKeeperZNodeComponent(ZooKeeperZNodeAsyncComponent znodeComponent) {
    this.znodeComponent = znodeComponent;
  }

  public List<String> getServiceCodes(String addresses) {
    CuratorFramework client = null;

    try {
      client = znodeComponent.createClient(addresses, RETRY_POLICY, ZNODE_CONNECTION_TIMEOUT_MS)
          .join();

      return znodeComponent.getNonReplServiceCodes(client)
          .thenCombine(znodeComponent.getReplServiceCodes(client),
              ListUtils::union)
          .orTimeout(ZNODE_TASK_TIMEOUT_MS, TimeUnit.MILLISECONDS)
          .join();
    } catch (Exception e) {
      throw new BusinessException(ZooKeeperApiErrorUtil.toErrorCode(e));
    } finally {
      if (client != null) {
        CloseableUtils.closeQuietly(client);
      }
    }
  }

}
