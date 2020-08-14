package com.jam2in.arcus.admin.tool.domain.agent.component;

import com.jam2in.arcus.admin.tool.domain.agent.dto.MemcachedOptionsDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AdminAgentAsyncComponent {

  private static final String START_ZOOKEEPER_API_FORMAT = "/api/v1/zkservers/%d/start";
  private static final String STOP_ZOOKEEPER_API_FORMAT = "/api/v1/zkservers/%d/stop";
  private static final String START_MEMCACHED_API_FORMAT = "/api/v1/mcservers/%d/start";
  private static final String STOP_MEMCACHED_API_FORMAT = "/api/v1/mcservers/%d/stop";

  public void startZooKeeperServer(String ip, int port) {
  }

  public void stopZooKeeperServer(String ip, int port) {
  }

  public void startMemcachedServer(String ip, int port,
                                   MemcachedOptionsDto memcachedOptionsDto) {
  }

  public void stopMemcachedServer(String ip, int port) {
  }

}
