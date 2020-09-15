package com.jam2in.arcus.admin.tool.domain.memcached.util;

import com.jam2in.arcus.admin.tool.error.ApiError;
import com.jam2in.arcus.admin.tool.error.ApiErrorCode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.TaskRejectedException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.concurrent.CompletionException;
import java.util.concurrent.TimeoutException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public final class CacheApiErrorUtil {

  public static ApiErrorCode toErrorCode(Throwable throwable) {
    if (throwable instanceof CompletionException) {
      return toErrorCode(throwable.getCause());
    }

    if (throwable instanceof TimeoutException) {
      return ApiErrorCode.MEMCACHED_TASK_TIMEOUT;
    } else if (throwable instanceof TaskRejectedException) {
      return ApiErrorCode.MEMCACHED_TASK_REJECTED;
    } else if (throwable instanceof ConnectException
        || throwable instanceof SocketTimeoutException) {
      return ApiErrorCode.MEMCACHED_CONNECTION_FAILED;
    } else {
      if (throwable != null) {
        log.error(throwable.getMessage(), throwable);
      }
      return ApiErrorCode.MEMCACHED_UNKNOWN;
    }
  }

  public static ApiError toError(Throwable throwable) {
    if (throwable == null) {
      return null;
    }

    return ApiError.of(toErrorCode(throwable));
  }

}
