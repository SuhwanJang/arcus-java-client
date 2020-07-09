package com.jam2in.arcus.admin.tool.domain.zookeeper.client;

import com.google.common.net.HostAndPort;
import com.jam2in.arcus.admin.tool.domain.zookeeper.exception.ZooKeeperException;
import com.jam2in.arcus.admin.tool.util.CommandSender;

public class ZooKeeperFourLetterWordImpl implements ZooKeeperFourLetterWord {

  @Override
  public String ruok(String address, int socketTimeoutMs) {
    return fourLetterWord(address, "ruok", socketTimeoutMs);
  }

  @Override
  public String srvr(String address, int socketTimeoutMs) {
    return fourLetterWord(address, "srvr", socketTimeoutMs);
  }

  @Override
  public String cons(String address, int socketTimeoutMs) {
    return fourLetterWord(address, "cons", socketTimeoutMs);
  }

  @Override
  public String mntr(String address, int socketTimeoutMs) {
    return fourLetterWord(address, "mntr", socketTimeoutMs);
  }

  @SuppressWarnings("UnstableApiUsage")
  private String fourLetterWord(String address, String command, int socketTimeoutMs) {
    try {
      HostAndPort hostAndPort = HostAndPort.fromString(address);

      return CommandSender.send(
          hostAndPort.getHost(), hostAndPort.getPort(),
          command, socketTimeoutMs);
    } catch (Exception e) {
      throw new ZooKeeperException(e);
    }
  }
}
