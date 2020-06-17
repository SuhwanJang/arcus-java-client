package com.jam2in.arcus.admin.tool.domain.ensemble.dto;

import com.jam2in.arcus.admin.tool.error.ApiError;
import com.jam2in.arcus.admin.tool.error.ApiErrorCode;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.TaskRejectedException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.Collection;
import java.util.concurrent.CompletionException;
import java.util.concurrent.TimeoutException;

@Getter
@Slf4j
public class ZooKeeperFourLetterDto {

  private final String ruok;

  private final ZooKeeperFourLetterSrvrDto srvr;

  private final Collection<ZooKeeperFourLetterConsDto> cons;

  private final ApiError error;

  @Builder
  public ZooKeeperFourLetterDto(String ruok,
                                ZooKeeperFourLetterSrvrDto srvr,
                                Collection<ZooKeeperFourLetterConsDto> cons,
                                Throwable throwable) {
    this.ruok = ruok;
    this.srvr = srvr;
    this.cons = cons;
    this.error = toError(throwable);
  }

  public static ApiErrorCode toErrorCode(Throwable throwable) {
    if (throwable instanceof TaskRejectedException) {
      return ApiErrorCode.ZOOKEEPER_TASK_REJECTED;
    } else if (throwable instanceof TimeoutException) {
      return ApiErrorCode.ZOOKEEPER_TASK_TIMEOUT;
    } else if (throwable instanceof CompletionException) {
      if (throwable.getCause() instanceof RuntimeException) {
        RuntimeException r = (RuntimeException) throwable.getCause();
        if (r.getCause() instanceof ConnectException) {
          return ApiErrorCode.ZOOKEEPER_CONNECTION_FAILED;
        } else if (r.getCause() instanceof SocketTimeoutException) {
          return ApiErrorCode.ZOOKEEPER_SOCKET_TIMEOUT;
        } else {
          log.info(throwable.getMessage(), throwable);
          return ApiErrorCode.ZOOKEEPER_UNKNOWN;
        }
      } else {
        log.info(throwable.getMessage(), throwable);
        return ApiErrorCode.ZOOKEEPER_UNKNOWN;
      }
    } else {
      log.info(throwable.getMessage(), throwable);
      return ApiErrorCode.ZOOKEEPER_UNKNOWN;
    }
  }

  public static ApiError toError(Throwable throwable) {
    if (throwable == null) {
      return null;
    }

    return ApiError.of(toErrorCode(throwable));
  }

}
