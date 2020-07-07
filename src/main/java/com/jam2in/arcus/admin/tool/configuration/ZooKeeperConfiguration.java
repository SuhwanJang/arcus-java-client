package com.jam2in.arcus.admin.tool.configuration;

import com.jam2in.arcus.admin.tool.domain.zookeeper.client.ZooKeeperClient;
import com.jam2in.arcus.admin.tool.domain.zookeeper.client.ZooKeeperCuratorClient;
import com.jam2in.arcus.admin.tool.domain.zookeeper.client.ZooKeeperFourLetterWord;
import com.jam2in.arcus.admin.tool.domain.zookeeper.client.ZooKeeperFourLetterWordMain;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ZooKeeperConfiguration {

  @Bean
  public ZooKeeperClient zookeeperClient() {
    return new ZooKeeperCuratorClient();
  }

  @Bean
  public ZooKeeperFourLetterWord zookeeperFourLetterWord() {
    return new ZooKeeperFourLetterWordMain();
  }

}
