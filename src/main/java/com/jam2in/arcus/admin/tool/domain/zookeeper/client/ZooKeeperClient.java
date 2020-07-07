package com.jam2in.arcus.admin.tool.domain.zookeeper.client;

import java.util.List;

public interface ZooKeeperClient {

  Object open(String address, int connectionTimeoutMs);

  void close(Object connection);

  List<String> get(Object connection, String path);

  void create(Object connection, String path);

  void delete(Object connection, String path);

}
