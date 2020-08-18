package com.jam2in.arcus.admin.tool.domain.agent.component;

import com.jam2in.arcus.admin.tool.domain.agent.dto.MemcachedOptionsDto;
import com.jam2in.arcus.admin.tool.error.ApiError;
import com.jam2in.arcus.admin.tool.error.ApiErrorCode;
import com.jam2in.arcus.admin.tool.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;

import java.net.ConnectException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;

@Component
@Slf4j
public class AdminAgentComponent {

  // TODO: use property value
  public static final int TASK_TIMEOUT_MS = 15000;

  private final AdminAgentAsyncComponent adminAgentComponent;

  private AdminAgentComponent(AdminAgentAsyncComponent adminAgentComponent) {
    this.adminAgentComponent = adminAgentComponent;
  }

  public void startZooKeeperServer(String address, int port, String token) {
    doRequest(() ->
        adminAgentComponent.startZooKeeperServer(address, port, token));
  }

  public void stopZooKeeperServer(String address, int port, String token) {
    doRequest(() ->
        adminAgentComponent.stopZooKeeperServer(address, port, token));
  }

  public void startMemcachedServer(String address, int port, String token,
                                   MemcachedOptionsDto memcachedOptionsDto) {
    doRequest(() ->
      adminAgentComponent.startMemcachedServer(address, port, token, memcachedOptionsDto));
  }

  public void stopMemcachedServer(String address, int port, String token) {
    doRequest(() ->
        adminAgentComponent.stopMemcachedServer(address, port, token));
  }

  private void doRequest(Supplier<CompletableFuture<ApiError>> supplier) {
    try {
      ApiError apiError = supplier.get()
          .orTimeout(TASK_TIMEOUT_MS, TimeUnit.MILLISECONDS)
          .exceptionally(this::toErrorCode)
          .join();

      if (apiError != null) {
        throw new BusinessException(apiError);
      }
    } catch (Throwable t) {
      if (t instanceof BusinessException) {
        throw t;
      } else {
        throw new BusinessException(toErrorCode(t));
      }
    }
  }

  private ApiError toErrorCode(Throwable throwable) {
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
