package com.jam2in.arcus.admin.tool.domain.agent.component;

import com.jam2in.arcus.admin.tool.domain.agent.dto.MemcachedOptionsDto;
import com.jam2in.arcus.admin.tool.domain.agent.util.AgentApiErrorUtils;
import com.jam2in.arcus.admin.tool.domain.zookeeper.dto.ZooKeeperParticipantsDto;
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
  public static final int TASK_TIMEOUT_MS = 6000;

  private final AdminAgentAsyncComponent adminAgentComponent;

  private AdminAgentComponent(AdminAgentAsyncComponent adminAgentComponent) {
    this.adminAgentComponent = adminAgentComponent;
  }

  public void installZooKeeperServer(String address, int port, String version,
                                     String token) {
    requestSync(() ->
        adminAgentComponent.installZooKeeperServer(address, port, version, token));
  }

  public CompletableFuture<ApiError> initializeZooKeeperServer(String address, int port,
                                                               ZooKeeperParticipantsDto zookeeperParticipantsDto,
                                                               String token) {
    return requestAsync(() ->
        adminAgentComponent.initializeZooKeeperServer(address, port, zookeeperParticipantsDto, token));
  }

  public void startZooKeeperServer(String address, int port,
                                   String token) {
    requestSync(() ->
        adminAgentComponent.startZooKeeperServer(address, port, token));
  }

  public void stopZooKeeperServer(String address, int port,
                                  String token) {
    requestSync(() ->
        adminAgentComponent.stopZooKeeperServer(address, port, token));
  }

  public void installMemcachedServer(String address, int port, String version,
                                     String token) {
    requestSync(() ->
        adminAgentComponent.installMemcachedServer(address, port, version, token));
  }

  public void startMemcachedServer(String address, int port,
                                   MemcachedOptionsDto memcachedOptionsDto,
                                   String token) {
    requestSync(() ->
        adminAgentComponent.startMemcachedServer(address, port, memcachedOptionsDto, token));
  }

  public void stopMemcachedServer(String address, int port, String token) {
    requestSync(() ->
        adminAgentComponent.stopMemcachedServer(address, port, token));
  }

  private void requestSync(Supplier<CompletableFuture<ApiError>> supplier) {
    ApiError apiError = requestAsync(supplier).join();

    if (apiError != null) {
      throw new BusinessException(apiError);
    }
  }

  private CompletableFuture<ApiError> requestAsync(Supplier<CompletableFuture<ApiError>> supplier) {
    try {
      return supplier.get()
          .orTimeout(TASK_TIMEOUT_MS, TimeUnit.MILLISECONDS)
          .exceptionally(AgentApiErrorUtils::toError);
    } catch (Throwable t) {
      return CompletableFuture.completedFuture(AgentApiErrorUtils.toError(t));
    }
  }

}
