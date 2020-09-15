package com.jam2in.arcus.admin.tool.domain.agent.component;

import com.jam2in.arcus.admin.tool.domain.agent.dto.MemcachedOptionsDto;
import com.jam2in.arcus.admin.tool.domain.memcached.util.CacheApiErrorUtil;
import com.jam2in.arcus.admin.tool.error.ApiError;
import com.jam2in.arcus.admin.tool.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
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
          .exceptionally(CacheApiErrorUtil::toError)
          .join();

      if (apiError != null) {
        throw new BusinessException(apiError);
      }
    } catch (Throwable t) {
      if (t instanceof BusinessException) {
        throw t;
      } else {
        throw new BusinessException(CacheApiErrorUtil.toErrorCode(t));
      }
    }
  }

}
