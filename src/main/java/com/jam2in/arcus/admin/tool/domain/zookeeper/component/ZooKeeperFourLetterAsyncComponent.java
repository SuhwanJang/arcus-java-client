package com.jam2in.arcus.admin.tool.domain.zookeeper.component;

import com.google.common.net.HostAndPort;
import com.jam2in.arcus.admin.tool.domain.zookeeper.exception.ZooKeeperException;
import com.jam2in.arcus.admin.tool.domain.zookeeper.util.FourLetterWordMain;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
public class ZooKeeperFourLetterAsyncComponent {

  @SuppressWarnings("UnstableApiUsage")
  private CompletableFuture<String> fourLetter(String address, String command,
                                               int socketTimeoutMs) {
    CompletableFuture<String> completableFuture = new CompletableFuture<>();

    try {
      HostAndPort hostAndPort = HostAndPort.fromString(address);

      completableFuture.complete(
          FourLetterWordMain.send4LetterWord(
              hostAndPort.getHost(), hostAndPort.getPort(),
              command, socketTimeoutMs));
    } catch (Exception e) {
      throw new ZooKeeperException(e);
    }

    return completableFuture;
  }

  @Async
  public CompletableFuture<String> ruok(String address, int socketTimeoutMs) {
    return fourLetter(address, "ruok",
        socketTimeoutMs);
  }

  @Async
  public CompletableFuture<String> srvr(String address, int socketTimeoutMs) {
    return fourLetter(address, "srvr",
        socketTimeoutMs);
  }

  @Async
  public CompletableFuture<String> cons(String address, int socketTimeoutMs) {
    return fourLetter(address, "cons",
        socketTimeoutMs);
  }

  @Async
  public CompletableFuture<String> mntr(String address, int socketTimeoutMs) {
    return fourLetter(address, "mntr",
        socketTimeoutMs);
  }

}
