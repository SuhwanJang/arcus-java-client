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
public final class MemcachedApiErrorUtils {

  public static ApiError toError(Throwable throwable) {
    if (throwable instanceof CompletionException) {
      return toError(throwable.getCause());
    }

    if (throwable instanceof TimeoutException) {
      return ApiError.of(ApiErrorCode.MEMCACHED_TASK_TIMEOUT);
    } else if (throwable instanceof TaskRejectedException) {
      return ApiError.of(ApiErrorCode.MEMCACHED_TASK_REJECTED);
    } else if (throwable instanceof ConnectException
        || throwable instanceof SocketTimeoutException) {
      return ApiError.of(ApiErrorCode.MEMCACHED_CONNECTION_FAILED);
    } else {
      if (throwable != null) {
        log.error(throwable.getMessage(), throwable);
      }
      return ApiError.of(ApiErrorCode.MEMCACHED_UNKNOWN);
    }
  }

}
