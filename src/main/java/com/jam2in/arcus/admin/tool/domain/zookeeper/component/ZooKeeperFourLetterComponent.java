package com.jam2in.arcus.admin.tool.domain.zookeeper.component;

import com.jam2in.arcus.admin.tool.domain.zookeeper.dto.ZooKeeperFourLetterConsDto;
import com.jam2in.arcus.admin.tool.domain.zookeeper.dto.ZooKeeperFourLetterDto;
import com.jam2in.arcus.admin.tool.domain.zookeeper.dto.ZooKeeperFourLetterMntrDto;
import com.jam2in.arcus.admin.tool.domain.zookeeper.dto.ZooKeeperFourLetterSrvrDto;
import com.jam2in.arcus.admin.tool.domain.zookeeper.parser.ZooKeeperFourLetterConsParser;
import com.jam2in.arcus.admin.tool.domain.zookeeper.parser.ZooKeeperFourLetterMntrParser;
import com.jam2in.arcus.admin.tool.domain.zookeeper.parser.ZooKeeperFourLetterRuokParser;
import com.jam2in.arcus.admin.tool.domain.zookeeper.parser.ZooKeeperFourLetterSrvrParser;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Component
public class ZooKeeperFourLetterComponent {

  // TODO: use property value
  public static final int FOUR_LETTER_SOCKET_TIMEOUT_MS = 10000;
  public static final int FOUR_LETTER_TASK_TIMEOUT_MS = 15000;

  private final ZooKeeperFourLetterAsyncComponent fourLetterComponent;

  public ZooKeeperFourLetterComponent(ZooKeeperFourLetterAsyncComponent fourLetterComponent) {
    this.fourLetterComponent = fourLetterComponent;
  }

  public ZooKeeperFourLetterSrvrDto getSrvr(String address) throws IOException {
    return fourLetterComponent.srvr(address, FOUR_LETTER_SOCKET_TIMEOUT_MS)
        .thenApply(ZooKeeperFourLetterSrvrParser::parse)
        .orTimeout(FOUR_LETTER_TASK_TIMEOUT_MS, TimeUnit.MILLISECONDS)
        .join();
  }

  public Collection<ZooKeeperFourLetterConsDto> getCons(String address) throws IOException {
    return fourLetterComponent.cons(address, FOUR_LETTER_SOCKET_TIMEOUT_MS)
        .thenApply(ZooKeeperFourLetterConsParser::parse)
        .orTimeout(FOUR_LETTER_TASK_TIMEOUT_MS, TimeUnit.MILLISECONDS)
        .join();
  }

  public ZooKeeperFourLetterMntrDto getMntr(String address) throws IOException {
    return fourLetterComponent.mntr(address, FOUR_LETTER_SOCKET_TIMEOUT_MS)
        .thenApply(ZooKeeperFourLetterMntrParser::parse)
        .orTimeout(FOUR_LETTER_TASK_TIMEOUT_MS, TimeUnit.MILLISECONDS)
        .join();
  }

  public CompletableFuture<ZooKeeperFourLetterDto> getStats(String address) {
    try {
      return fourLetterComponent.ruok(address, FOUR_LETTER_SOCKET_TIMEOUT_MS)
          .thenApply(ruok ->
              ZooKeeperFourLetterDto.builder().ruok(ZooKeeperFourLetterRuokParser.parse(ruok)))
          .thenCombine(
              fourLetterComponent.srvr(address, FOUR_LETTER_SOCKET_TIMEOUT_MS),
              (builder, srvr) ->
                  builder.srvr(ZooKeeperFourLetterSrvrParser.parse(srvr)).build())
          .orTimeout(FOUR_LETTER_TASK_TIMEOUT_MS, TimeUnit.MILLISECONDS)
          .exceptionally(throwable ->
              ZooKeeperFourLetterDto.builder().throwable(throwable).build());
    } catch (Exception e) {
      return CompletableFuture.completedFuture(
          ZooKeeperFourLetterDto.builder().throwable(e).build());
    }
  }

}
