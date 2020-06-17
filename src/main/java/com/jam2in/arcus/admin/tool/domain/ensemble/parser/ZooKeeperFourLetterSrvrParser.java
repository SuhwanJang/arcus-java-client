package com.jam2in.arcus.admin.tool.domain.ensemble.parser;

import com.jam2in.arcus.admin.tool.domain.ensemble.dto.ZooKeeperFourLetterSrvrDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.util.stream.Stream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public final class ZooKeeperFourLetterSrvrParser {

  /*
    Zookeeper version: 3.4.5-p6--1, built on 06/08/2020 05:07 GMT
    Latency min/avg/max: 0/0/0
    Received: 15
    Sent: 14
    Connections: 1
    Outstanding: 0
    Zxid: 0x400000033
    Mode: follower
    Node count: 72
  */

  private static final String LINE_ZOOKEEPER_VERSION = "Zookeeper version: ";
  private static final String LINE_LATENCY = "Latency min/avg/max: ";
  private static final String LINE_RECEIVED = "Received: ";
  private static final String LINE_SENT = "Sent: ";
  private static final String LINE_CONNECTIONS = "Connections: ";
  private static final String LINE_OUTSTANDING = "Outstanding: ";
  private static final String LINE_ZXID = "Zxid: ";
  private static final String LINE_MODE = "Mode: ";
  private static final String LINE_NODE_COUNT = "Node count: ";

  private static void parseZooKeeperVersion(
      ZooKeeperFourLetterSrvrDto.ZooKeeperFourLetterSrvrDtoBuilder builder,
      String line) {
    builder.version(StringUtils.substringAfter(line, LINE_ZOOKEEPER_VERSION).split(",")[0]);
  }

  private static void parseLatency(
      ZooKeeperFourLetterSrvrDto.ZooKeeperFourLetterSrvrDtoBuilder builder,
      String line) {
    String[] splitted = StringUtils.substringAfter(line, LINE_LATENCY).split("/");

    builder.latencyMin(splitted[0]);
    builder.latencyAvg(splitted[1]);
    builder.latencyMax(splitted[2]);
  }

  private static void parseReceived(
      ZooKeeperFourLetterSrvrDto.ZooKeeperFourLetterSrvrDtoBuilder builder,
      String line) {
    builder.received(StringUtils.substringAfter(line, LINE_RECEIVED));
  }

  private static void parseSent(
      ZooKeeperFourLetterSrvrDto.ZooKeeperFourLetterSrvrDtoBuilder builder,
      String line) {
    builder.sent(StringUtils.substringAfter(line, LINE_SENT));
  }

  private static void parseConnections(
      ZooKeeperFourLetterSrvrDto.ZooKeeperFourLetterSrvrDtoBuilder builder,
      String line) {
    builder.connections(StringUtils.substringAfter(line, LINE_CONNECTIONS));
  }

  private static void parseOutstanding(
      ZooKeeperFourLetterSrvrDto.ZooKeeperFourLetterSrvrDtoBuilder builder,
      String line) {
    builder.outstanding(StringUtils.substringAfter(line, LINE_OUTSTANDING));
  }

  private static void parseZxid(
      ZooKeeperFourLetterSrvrDto.ZooKeeperFourLetterSrvrDtoBuilder builder,
      String line) {
    builder.zxId(StringUtils.substringAfter(line, LINE_ZXID));
  }

  private static void parseMode(
      ZooKeeperFourLetterSrvrDto.ZooKeeperFourLetterSrvrDtoBuilder builder,
      String line) {
    builder.mode(StringUtils.substringAfter(line, LINE_MODE));
  }

  private static void parseNodeCount(
      ZooKeeperFourLetterSrvrDto.ZooKeeperFourLetterSrvrDtoBuilder builder,
      String line) {
    builder.nodeCount(StringUtils.substringAfter(line, LINE_NODE_COUNT));
  }

  public static ZooKeeperFourLetterSrvrDto parse(String srvr) {
    ZooKeeperFourLetterSrvrDto.ZooKeeperFourLetterSrvrDtoBuilder builder =
        ZooKeeperFourLetterSrvrDto.builder();

    Stream.of(srvr.split("\n"))
        .forEach(line -> {
          try {
            if (line.startsWith(LINE_ZOOKEEPER_VERSION)) {
              parseZooKeeperVersion(builder, line);
            } else if (line.startsWith(LINE_LATENCY)) {
              parseLatency(builder, line);
            } else if (line.startsWith(LINE_RECEIVED)) {
              parseReceived(builder, line);
            } else if (line.startsWith(LINE_SENT)) {
              parseSent(builder, line);
            } else if (line.startsWith(LINE_CONNECTIONS)) {
              parseConnections(builder, line);
            } else if (line.startsWith(LINE_OUTSTANDING)) {
              parseOutstanding(builder, line);
            } else if (line.startsWith(LINE_ZXID)) {
              parseZxid(builder, line);
            } else if (line.startsWith(LINE_MODE)) {
              parseMode(builder, line);
            } else if (line.startsWith(LINE_NODE_COUNT)) {
              parseNodeCount(builder, line);
            }
          } catch (Exception e) {
            log.error("zookeeper four letter command('srvr') parsing error", e);
          }
        });

    return builder.build();
  }

}
