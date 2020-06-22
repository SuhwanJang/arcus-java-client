package com.jam2in.arcus.admin.tool.domain.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.utils.CloseableUtils;
import org.apache.curator.x.async.AsyncCuratorFramework;
import org.apache.zookeeper.KeeperException;
import org.junit.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MyTest {

  @Test
  public void test() throws Exception {
    /*
     * ip:port,ip:port... 접근시도
     * 1초 3번 연결 재시도
     * 연결,세션 timeout 3초
     * 요건 튜토리얼에 있던거
     */

//    CuratorFramework client = CuratorFrameworkFactory.newClient("1.255.51.181:7189", new RetryNTimes(100, 3));
//    CuratorFramework client = CuratorFrameworkFactory.newClient("192.168.0.234:2181,192.168.0.235:2181,192.168.0.236:2181", new RetryNTimes(100, 3));
    CuratorFramework client = CuratorFrameworkFactory.builder()
        .connectString("1.255.51.181:7189")
        .retryPolicy(new RetryNTimes(100, 3))
        .connectionTimeoutMs(2000)
        .build();
    client.start();

    AsyncCuratorFramework async = AsyncCuratorFramework.wrap(client);

    try {
      List<String> lists = async.getChildren().forPath("/arcus/cache_list")
          .toCompletableFuture()
          .thenCombine(
              async.getChildren().forPath("/arcus_repl/cache_list"),
              (a, b) -> {
                System.out.print("a=" + a);
                System.out.println("b=" + b);
                return ListUtils.union(a, b);
              }
          )
          .orTimeout(3, TimeUnit.SECONDS)
          .join();
      System.out.println(lists);
    } catch (Exception e) {
      System.out.println("hello??");
    } finally {
      CloseableUtils.closeQuietly(client);
    }
  }

}
