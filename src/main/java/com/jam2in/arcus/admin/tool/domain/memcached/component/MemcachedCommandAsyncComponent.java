package com.jam2in.arcus.admin.tool.domain.memcached.component;

import com.jam2in.arcus.admin.tool.util.CommandSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@Component
public class MemcachedCommandAsyncComponent {

  @Async
  public CompletableFuture<String> stats(String address, int socketTimeoutMs) throws IOException {
    return CompletableFuture.completedFuture(
        CommandSender.send(address, "stats\r\n", socketTimeoutMs));
  }

}
