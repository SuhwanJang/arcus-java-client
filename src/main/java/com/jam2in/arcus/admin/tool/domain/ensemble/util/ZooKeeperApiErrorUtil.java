package com.jam2in.arcus.admin.tool.domain.ensemble.util;

import com.jam2in.arcus.admin.tool.domain.ensemble.exception.ZooKeeperException;
import com.jam2in.arcus.admin.tool.error.ApiError;
import com.jam2in.arcus.admin.tool.error.ApiErrorCode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.KeeperException;
import org.springframework.core.task.TaskRejectedException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.concurrent.CompletionException;
import java.util.concurrent.TimeoutException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public final class ZooKeeperApiErrorUtil {

  public static ApiErrorCode toErrorCode(Throwable throwable) {
    if (throwable instanceof TaskRejectedException) {
      return ApiErrorCode.ZOOKEEPER_TASK_REJECTED;
    } else if (throwable instanceof TimeoutException) {
      return ApiErrorCode.ZOOKEEPER_TASK_TIMEOUT;
    } else if (throwable instanceof CompletionException) {
      if (throwable.getCause() instanceof TaskRejectedException) {
        return ApiErrorCode.ZOOKEEPER_TASK_REJECTED;
      } else if (throwable.getCause() instanceof TimeoutException) {
        return ApiErrorCode.ZOOKEEPER_TASK_TIMEOUT;
      } else if (throwable.getCause() instanceof ZooKeeperException) {
        ZooKeeperException r = (ZooKeeperException) throwable.getCause();
        if (r.getCause() instanceof ConnectException
            || r.getCause() instanceof SocketTimeoutException) {
          return ApiErrorCode.ZOOKEEPER_CONNECTION_FAILED;
        } else {
          log.info(throwable.getMessage(), throwable);
          return ApiErrorCode.ZOOKEEPER_UNKNOWN;
        }
      } else if (throwable.getCause() instanceof KeeperException) {
        if (!(throwable.getCause() instanceof KeeperException.ConnectionLossException)) {
          log.info(throwable.getMessage(), throwable);
        }
        return ApiErrorCode.ZOOKEEPER_CONNECTION_FAILED;
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
