package com.jam2in.arcus.admin.tool.domain.zookeeper.client;

public interface ZooKeeperFourLetterWord {

  String ruok(String address, int socketTimeoutMs);

  String srvr(String address, int socketTimeoutMs);

  String cons(String address, int socketTimeoutMs);

  String mntr(String address, int socketTimeoutMs);

}
