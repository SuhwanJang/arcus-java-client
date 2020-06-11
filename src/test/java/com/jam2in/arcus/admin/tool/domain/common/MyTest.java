package com.jam2in.arcus.admin.tool.domain.common;

import org.apache.curator.CuratorZookeeperClient;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.client.FourLetterWordMain;
import org.apache.zookeeper.common.X509Exception;
import org.apache.zookeeper.server.ZooKeeperServer;
import org.apache.zookeeper.server.ZooKeeperServerBean;
import org.apache.zookeeper.server.ZooKeeperServerMain;
import org.apache.zookeeper.server.command.FourLetterCommands;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MyTest {

  @Test
  public void test() throws IOException, InterruptedException, X509Exception.SSLContextException {
    /*
     * ip:port,ip:port... 접근시도
     * 1초 3번 연결 재시도
     * 연결,세션 timeout 3초
     * 요건 튜토리얼에 있던거
     */

    /*
    CuratorFramework client = CuratorFrameworkFactory.builder()
        .connectString("192.168.0.234:2181,192.168.0.235:2181,192.168.0.236:2181")
        .retryPolicy(new ExponentialBackoffRetry(1000, 3))
        .connectionTimeoutMs(10000)
        .sessionTimeoutMs(3000)
        .build();

    client.start();

    if (!client.getZookeeperClient().blockUntilConnectedOrTimedOut()) {
      System.out.println("shit");
      return;
    }
     */
  }

}
