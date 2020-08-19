package com.jam2in.arcus.admin.tool.domain.agent.util;

import com.jam2in.arcus.admin.tool.error.ApiError;
import com.jam2in.arcus.admin.tool.error.ApiErrorCode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.web.client.ResourceAccessException;

import java.net.ConnectException;
import java.util.concurrent.CompletionException;
import java.util.concurrent.TimeoutException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class AgentApiErrorUtil {

  public static ApiError toErrorCode(Throwable throwable) {
    if (throwable instanceof CompletionException
        || throwable instanceof ResourceAccessException) {
      return toErrorCode(throwable.getCause());
    }

    if (throwable instanceof TimeoutException) {
      return ApiError.of(ApiErrorCode.AGENT_TASK_TIMEOUT);
    } else if (throwable instanceof TaskRejectedException) {
      return ApiError.of(ApiErrorCode.AGENT_TASK_REJECTED);
    } else if (throwable instanceof ConnectException) {
      return ApiError.of(ApiErrorCode.AGENT_CONNECTION_FAILED);
    }

    if (throwable != null) {
      log.error(throwable.getMessage(), throwable);
    }

    return ApiError.of(ApiErrorCode.AGENT_UNKNOWN);
  }

}
