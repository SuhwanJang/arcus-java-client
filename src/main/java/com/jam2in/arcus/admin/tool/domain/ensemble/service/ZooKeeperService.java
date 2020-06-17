package com.jam2in.arcus.admin.tool.domain.ensemble.service;

import com.jam2in.arcus.admin.tool.domain.ensemble.component.ZooKeeperFourLetterComponent;
import com.jam2in.arcus.admin.tool.domain.ensemble.dto.ZooKeeperDto;
import com.jam2in.arcus.admin.tool.domain.ensemble.dto.ZooKeeperFourLetterConsDto;
import com.jam2in.arcus.admin.tool.domain.ensemble.dto.ZooKeeperFourLetterDto;
import com.jam2in.arcus.admin.tool.domain.ensemble.dto.ZooKeeperFourLetterSrvrDto;
import com.jam2in.arcus.admin.tool.domain.ensemble.entity.ZooKeeperEntity;
import com.jam2in.arcus.admin.tool.domain.ensemble.parser.ZooKeeperFourLetterConsParser;
import com.jam2in.arcus.admin.tool.domain.ensemble.parser.ZooKeeperFourLetterRuokParser;
import com.jam2in.arcus.admin.tool.domain.ensemble.parser.ZooKeeperFourLetterSrvrParser;
import com.jam2in.arcus.admin.tool.domain.ensemble.repository.ZooKeeperRepository;
import com.jam2in.arcus.admin.tool.error.ApiErrorCode;
import com.jam2in.arcus.admin.tool.exception.BusinessException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class ZooKeeperService {

  private static final int FOUR_LETTER_SOCKET_TIMEOUT_MS = 3000;
  private static final int FOUR_LETTER_EXECUTE_TIMEOUT_MS = 5000;

  private final EnsembleService ensembleService;

  private final ZooKeeperRepository zookeeperRepository;

  private final ZooKeeperFourLetterComponent zkFourLetterComponent;

  public ZooKeeperService(EnsembleService ensembleService,
                          ZooKeeperRepository zookeeperRepository,
                          ZooKeeperFourLetterComponent zkFourLetterComponent) {
    this.ensembleService = ensembleService;
    this.zookeeperRepository = zookeeperRepository;
    this.zkFourLetterComponent = zkFourLetterComponent;
  }

  public ZooKeeperDto get(long id) {
    return ZooKeeperDto.of(getEntity(id));
  }

  public ZooKeeperFourLetterSrvrDto getSrvr(long id) {
    try {
      return zkFourLetterComponent.srvr(getEntity(id).getAddress(), FOUR_LETTER_SOCKET_TIMEOUT_MS)
          .thenApply(ZooKeeperFourLetterSrvrParser::parse)
          .orTimeout(FOUR_LETTER_EXECUTE_TIMEOUT_MS, TimeUnit.MILLISECONDS)
          .join();
    } catch (Exception e) {
      throw new BusinessException(ZooKeeperFourLetterDto.toErrorCode(e));
    }
  }

  public Collection<ZooKeeperFourLetterConsDto> getCons(long id) {
    try {
      return zkFourLetterComponent.cons(getEntity(id).getAddress(), FOUR_LETTER_SOCKET_TIMEOUT_MS)
          .thenApply(ZooKeeperFourLetterConsParser::parse)
          .orTimeout(FOUR_LETTER_EXECUTE_TIMEOUT_MS, TimeUnit.MILLISECONDS)
          .join();
    } catch (Exception e) {
      throw new BusinessException(ZooKeeperFourLetterDto.toErrorCode(e));
    }
  }

  public Collection<ZooKeeperDto> getAllStats(long id) {
    return ensembleService.getZooKeepers(id)
        .stream()
        .map(this::getStats)
        .collect(Collectors.toList())
        .stream()
        .map(CompletableFuture::join)
        .collect(Collectors.toList());
  }

  private ZooKeeperEntity getEntity(long id) {
    return zookeeperRepository.findById(id)
        .orElseThrow(() -> new BusinessException(ApiErrorCode.ZOOKEEPER_NOT_FOUND));
  }

  private CompletableFuture<ZooKeeperDto> getStats(ZooKeeperDto zookeeperDto) {
    try {
      return zkFourLetterComponent.ruok(zookeeperDto.getAddress(), FOUR_LETTER_SOCKET_TIMEOUT_MS)
          .thenApply(ruok ->
              ZooKeeperFourLetterDto.builder().ruok(ZooKeeperFourLetterRuokParser.parse(ruok)))
          .thenCombine(
              zkFourLetterComponent.srvr(zookeeperDto.getAddress(), FOUR_LETTER_SOCKET_TIMEOUT_MS),
              (builder, srvr) ->
                  builder.srvr(ZooKeeperFourLetterSrvrParser.parse(srvr)))
          .thenApply(builder -> {
            zookeeperDto.setStats(builder.build());
            return zookeeperDto;
          })
          .orTimeout(FOUR_LETTER_EXECUTE_TIMEOUT_MS, TimeUnit.MILLISECONDS)
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
