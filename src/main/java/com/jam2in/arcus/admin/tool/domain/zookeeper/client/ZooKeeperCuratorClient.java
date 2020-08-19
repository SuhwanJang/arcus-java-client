package com.jam2in.arcus.admin.tool.domain.zookeeper.client;

import com.jam2in.arcus.admin.tool.domain.zookeeper.exception.ZooKeeperException;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.utils.CloseableUtils;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;

import java.net.ConnectException;
import java.util.Collections;
import java.util.List;

public class ZooKeeperCuratorClient implements ZooKeeperClient {

  @Override
  public Object open(String address, int connectionTimeoutMs) {
    CuratorFramework client = null;

    try {
      client = CuratorFrameworkFactory.builder()
          .connectString(address)
          .retryPolicy(new RetryNTimes(100, 3))
          .connectionTimeoutMs(connectionTimeoutMs)
          .zk34CompatibilityMode(true)
          .build();

      client.start();

      if (!client.getZookeeperClient().blockUntilConnectedOrTimedOut()) {
        throw new ZooKeeperException(new ConnectException());
      }

      return client;
    } catch (Exception e) {
      close(client);
      if (e instanceof ZooKeeperException) {
        throw (ZooKeeperException) e;
      }
      throw new ZooKeeperException(e);
    }
  }

  @Override
  public void close(Object connection) {
    CuratorFramework curator = (CuratorFramework) connection;

    if (connection != null) {
      CloseableUtils.closeQuietly(curator);
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<String> get(Object connection, String path) {
    CuratorFramework curator = (CuratorFramework) connection;

    try {
      return curator.getChildren().forPath(path);
    } catch (Exception e) {
      if (e instanceof KeeperException.NoNodeException) {
        return Collections.EMPTY_LIST;
      } else {
        throw new ZooKeeperException(e);
      }
    }
  }

  @Override
  public void create(Object connection, String path) {
    CuratorFramework curator = (CuratorFramework) connection;

    try {
      curator.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path);
    } catch (Exception e) {
      if (!(e instanceof KeeperException.NodeExistsException)) {
        throw new ZooKeeperException(e);
      }
    }
  }

  @Override
  public void delete(Object connection, String path) {
    CuratorFramework curator = (CuratorFramework) connection;

    try {
      curator.delete().deletingChildrenIfNeeded().forPath(path);
    } catch (Exception e) {
      if (!(e instanceof KeeperException.NoNodeException)) {
        throw new ZooKeeperException(e);
      }
    }
  }

}
