package com.jam2in.arcus.admin.tool.domain.zookeeper.component;

import com.jam2in.arcus.admin.tool.util.CommandSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
public class ZooKeeperFourLetterAsyncComponent {

  @Async
  public CompletableFuture<String> ruok(String address, int socketTimeoutMs) throws IOException {
    return CompletableFuture.completedFuture(
        CommandSender.send(address, "ruok", socketTimeoutMs));
  }

  @Async
  public CompletableFuture<String> srvr(String address, int socketTimeoutMs) throws IOException {
    return CompletableFuture.completedFuture(
        CommandSender.send(address, "srvr", socketTimeoutMs));
  }

  @Async
  public CompletableFuture<String> cons(String address, int socketTimeoutMs) throws IOException {
    return CompletableFuture.completedFuture(
        CommandSender.send(address, "cons", socketTimeoutMs));
  }

  @Async
  public CompletableFuture<String> mntr(String address, int socketTimeoutMs) throws IOException {
    return CompletableFuture.completedFuture(
        CommandSender.send(address, "mntr", socketTimeoutMs));
  }
}
