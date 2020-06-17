package com.jam2in.arcus.admin.tool.domain.ensemble.component;

import com.google.common.net.HostAndPort;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.client.FourLetterWordMain;
import org.apache.zookeeper.server.command.FourLetterCommands;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
public class ZooKeeperFourLetterComponent {

  @SuppressWarnings("UnstableApiUsage")
  private CompletableFuture<String> fourLetter(String address, String command,
                                               int socketTimeoutMs) {
    CompletableFuture<String> completableFuture = new CompletableFuture<>();

    try {
      HostAndPort hostAndPort = HostAndPort.fromString(address);

      completableFuture.complete(
          FourLetterWordMain.send4LetterWord(
              hostAndPort.getHost(), hostAndPort.getPort(),
              command,
              false, socketTimeoutMs));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    return completableFuture;
  }

  @Async
  public CompletableFuture<String> ruok(String address, int socketTimeoutMs) {
    return fourLetter(address, FourLetterCommands.getCommandString(FourLetterCommands.ruokCmd),
        socketTimeoutMs);
  }

  @Async
  public CompletableFuture<String> srvr(String address, int socketTimeoutMs) {
    return fourLetter(address, FourLetterCommands.getCommandString(FourLetterCommands.srvrCmd),
        socketTimeoutMs);
  }

  @Async
  public CompletableFuture<String> cons(String address, int socketTimeoutMs) {
    return fourLetter(address, FourLetterCommands.getCommandString(FourLetterCommands.consCmd),
        socketTimeoutMs);
  }

  @Async
  public CompletableFuture<String> mntr(String address, int socketTimeoutMs) {
    return fourLetter(address, FourLetterCommands.getCommandString(FourLetterCommands.mntrCmd),
        socketTimeoutMs);
  }

}
