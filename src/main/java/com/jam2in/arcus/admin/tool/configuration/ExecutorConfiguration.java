package com.jam2in.arcus.admin.tool.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class ExecutorConfiguration {

  @Value("${executor.core-pool-size}")
  private int corePoolSize;

  @Value("${executor.max-pool-size}")
  private int maxPoolSize;

  @Value("${executor.queue-capacity}")
  private int queueCapacity;

  @Value("${executor.keep-alive-seconds}")
  private int keepAliveSeconds;

  @Value("${executor.prefix}")
  private String prefix;

  @Bean
  public Executor threadPoolTaskExecutor() {
    ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
    threadPoolTaskExecutor.setCorePoolSize(corePoolSize);
    threadPoolTaskExecutor.setMaxPoolSize(maxPoolSize);
    threadPoolTaskExecutor.setQueueCapacity(queueCapacity);
    threadPoolTaskExecutor.setKeepAliveSeconds(keepAliveSeconds);
    threadPoolTaskExecutor.setThreadNamePrefix(prefix);
    threadPoolTaskExecutor.initialize();

    return threadPoolTaskExecutor;
  }

}
