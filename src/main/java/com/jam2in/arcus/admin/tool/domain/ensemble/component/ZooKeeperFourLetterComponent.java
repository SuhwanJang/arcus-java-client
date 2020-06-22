package com.jam2in.arcus.admin.tool.domain.ensemble.component;

import com.jam2in.arcus.admin.tool.domain.ensemble.dto.ZooKeeperDto;
import com.jam2in.arcus.admin.tool.domain.ensemble.dto.ZooKeeperFourLetterConsDto;
import com.jam2in.arcus.admin.tool.domain.ensemble.dto.ZooKeeperFourLetterDto;
import com.jam2in.arcus.admin.tool.domain.ensemble.dto.ZooKeeperFourLetterMntrDto;
import com.jam2in.arcus.admin.tool.domain.ensemble.dto.ZooKeeperFourLetterSrvrDto;
import com.jam2in.arcus.admin.tool.domain.ensemble.parser.ZooKeeperFourLetterConsParser;
import com.jam2in.arcus.admin.tool.domain.ensemble.parser.ZooKeeperFourLetterMntrParser;
import com.jam2in.arcus.admin.tool.domain.ensemble.parser.ZooKeeperFourLetterRuokParser;
import com.jam2in.arcus.admin.tool.domain.ensemble.parser.ZooKeeperFourLetterSrvrParser;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class ZooKeeperFourLetterComponent {

  // TODO: use property value
  public static final int FOUR_LETTER_SOCKET_TIMEOUT_MS = 3000;
  public static final int FOUR_LETTER_TASK_TIMEOUT_MS = 6000;

  private final ZooKeeperFourLetterAsyncComponent fourLetterComponent;

  public ZooKeeperFourLetterComponent(ZooKeeperFourLetterAsyncComponent fourLetterComponent) {
    this.fourLetterComponent = fourLetterComponent;
  }

  public ZooKeeperFourLetterSrvrDto getSrvr(String address) {
    return fourLetterComponent.srvr(address, FOUR_LETTER_SOCKET_TIMEOUT_MS)
        .thenApply(ZooKeeperFourLetterSrvrParser::parse)
        .orTimeout(FOUR_LETTER_TASK_TIMEOUT_MS, TimeUnit.MILLISECONDS)
        .join();
  }

  public Collection<ZooKeeperFourLetterConsDto> getCons(String address) {
    return fourLetterComponent.cons(address, FOUR_LETTER_SOCKET_TIMEOUT_MS)
        .thenApply(ZooKeeperFourLetterConsParser::parse)
        .orTimeout(FOUR_LETTER_TASK_TIMEOUT_MS, TimeUnit.MILLISECONDS)
        .join();
  }

  public ZooKeeperFourLetterMntrDto getMntr(String address) {
    return fourLetterComponent.mntr(address, FOUR_LETTER_SOCKET_TIMEOUT_MS)
        .thenApply(ZooKeeperFourLetterMntrParser::parse)
        .orTimeout(FOUR_LETTER_TASK_TIMEOUT_MS, TimeUnit.MILLISECONDS)
        .join();
  }

  public Collection<ZooKeeperDto> getAllStats(Collection<ZooKeeperDto> zookeepers) {
    return zookeepers.stream()
        .map(this::getStats)
        .collect(Collectors.toList())
        .stream()
        .map(CompletableFuture::join)
        .collect(Collectors.toList());
  }

  private CompletableFuture<ZooKeeperDto> getStats(ZooKeeperDto zookeeperDto) {
    try {
      return fourLetterComponent.ruok(zookeeperDto.getAddress(), FOUR_LETTER_SOCKET_TIMEOUT_MS)
          .thenApply(ruok ->
              ZooKeeperFourLetterDto.builder().ruok(ZooKeeperFourLetterRuokParser.parse(ruok)))
          .thenCombine(
              fourLetterComponent.srvr(zookeeperDto.getAddress(), FOUR_LETTER_SOCKET_TIMEOUT_MS),
              (builder, srvr) ->
                  builder.srvr(ZooKeeperFourLetterSrvrParser.parse(srvr)))
          .thenApply(builder -> {
            zookeeperDto.setStats(builder.build());
            return zookeeperDto;
          })
          .orTimeout(FOUR_LETTER_TASK_TIMEOUT_MS, TimeUnit.MILLISECONDS)
          .exceptionally(throwable -> {
            zookeeperDto.setStats(ZooKeeperFourLetterDto.builder().throwable(throwable).build());
            return zookeeperDto;
          });
    } catch (Exception e) {
      zookeeperDto.setStats(ZooKeeperFourLetterDto.builder().throwable(e).build());
      return CompletableFuture.completedFuture(zookeeperDto);
    }
  }

}
