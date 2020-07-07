package com.jam2in.arcus.admin.tool.domain.zookeeper.component;

import com.jam2in.arcus.admin.tool.domain.zookeeper.client.ZooKeeperFourLetterWord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
public class ZooKeeperFourLetterAsyncComponent {

  private final ZooKeeperFourLetterWord fourLetterWord;

  public ZooKeeperFourLetterAsyncComponent(ZooKeeperFourLetterWord fourLetterWord) {
    this.fourLetterWord = fourLetterWord;
  }

  @Async
  public CompletableFuture<String> ruok(String address, int socketTimeoutMs) {
    return CompletableFuture.completedFuture(
        fourLetterWord.ruok(address,  socketTimeoutMs));
  }

  @Async
  public CompletableFuture<String> srvr(String address, int socketTimeoutMs) {
    return CompletableFuture.completedFuture(
        fourLetterWord.srvr(address,  socketTimeoutMs));
  }

  @Async
  public CompletableFuture<String> cons(String address, int socketTimeoutMs) {
    return CompletableFuture.completedFuture(
        fourLetterWord.cons(address,  socketTimeoutMs));
  }

  @Async
  public CompletableFuture<String> mntr(String address, int socketTimeoutMs) {
    return CompletableFuture.completedFuture(
        fourLetterWord.mntr(address,  socketTimeoutMs));
  }

}
