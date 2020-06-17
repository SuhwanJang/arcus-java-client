package com.jam2in.arcus.admin.tool.domain.ensemble.parser;

import com.jam2in.arcus.admin.tool.domain.ensemble.dto.ZooKeeperFourLetterMntrDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.util.function.Consumer;
import java.util.stream.Stream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public final class ZooKeeperFourLetterMntrParser {

  /*
    zk_version 3.4.5-p6--1, built on 06/08/2020 05:07 GMT
    zk_avg_latency 0
    zk_max_latency 18
    zk_min_latency 0
    zk_packets_received 4266
    zk_packets_sent 4262
    zk_num_alive_connections 2
    zk_outstanding_requests 0
    zk_server_state follower
    zk_znode_count 57
    zk_watch_count 2
    zk_ephemerals_count 2
    zk_approximate_data_size 4484
    zk_open_file_descriptor_count 31
    zk_max_file_descriptor_count 4096
   */

  private static final String LINE_VERSION = "zk_version";
  private static final String LINE_AVERAGE_LATENCY = "zk_avg_latency";
  private static final String LINE_MAX_LATENCY = "zk_max_latency";
  private static final String LINE_MIN_LATENCY = "zk_min_latency";
  private static final String LINE_PACKETS_RECEIVED = "zk_packets_received";
  private static final String LINE_PACKETS_SENT = "zk_packets_sent";
  private static final String LINE_NUM_ALIVE_CONNECTIONS = "zk_num_alive_connections";
  private static final String LINE_OUTSTANDING_REQUESTS = "zk_outstanding_requests";
  private static final String LINE_SERVER_STATE = "zk_server_state";
  private static final String LINE_ZNODE_COUNT = "zk_znode_count";
  private static final String LINE_WATCH_COUNT = "zk_watch_count";
  private static final String LINE_EPHEMERALS_COUNT = "zk_ephemerals_count";
  private static final String LINE_APPROXIMATE_DATA_SIZE = "zk_approximate_data_size";
  private static final String LINE_OPEN_FILE_DESCRIPTOR_COUNT = "zk_open_file_descriptor_count";
  private static final String LINE_MAX_FILE_DESCRIPTOR_COUNT = "zk_max_file_descriptor_count";

  private static void parse(String line, String seperator, Consumer<String> consumer) {
    consumer.accept(StringUtils.trim(StringUtils.substringAfter(line, seperator)));
  }

  public static ZooKeeperFourLetterMntrDto parse(String mntr) {
    ZooKeeperFourLetterMntrDto.ZooKeeperFourLetterMntrDtoBuilder builder =
        ZooKeeperFourLetterMntrDto.builder();

    Stream.of(mntr.split("\n"))
        .forEach(line -> {
          try {
            if (line.startsWith(LINE_VERSION)) {
              parse(line, LINE_VERSION, builder::version);
            } else if (line.startsWith(LINE_AVERAGE_LATENCY)) {
              parse(line, LINE_AVERAGE_LATENCY, builder::averageLatency);
            } else if (line.startsWith(LINE_MAX_LATENCY)) {
              parse(line, LINE_MAX_LATENCY, builder::maxLatency);
            } else if (line.startsWith(LINE_MIN_LATENCY)) {
              parse(line, LINE_MIN_LATENCY, builder::minLatency);
            } else if (line.startsWith(LINE_PACKETS_RECEIVED)) {
              parse(line, LINE_PACKETS_RECEIVED, builder::packetsReceived);
            } else if (line.startsWith(LINE_PACKETS_SENT)) {
              parse(line, LINE_PACKETS_SENT, builder::packetsSent);
            } else if (line.startsWith(LINE_NUM_ALIVE_CONNECTIONS)) {
              parse(line, LINE_NUM_ALIVE_CONNECTIONS, builder::numAliveConnections);
            } else if (line.startsWith(LINE_OUTSTANDING_REQUESTS)) {
              parse(line, LINE_OUTSTANDING_REQUESTS, builder::outstandingRequests);
            } else if (line.startsWith(LINE_SERVER_STATE)) {
              parse(line, LINE_SERVER_STATE, builder::serverState);
            } else if (line.startsWith(LINE_ZNODE_COUNT)) {
              parse(line, LINE_ZNODE_COUNT, builder::znodeCount);
            } else if (line.startsWith(LINE_WATCH_COUNT)) {
              parse(line, LINE_WATCH_COUNT, builder::watchCount);
            } else if (line.startsWith(LINE_EPHEMERALS_COUNT)) {
              parse(line, LINE_EPHEMERALS_COUNT, builder::ephemeralsCount);
            } else if (line.startsWith(LINE_APPROXIMATE_DATA_SIZE)) {
              parse(line, LINE_APPROXIMATE_DATA_SIZE, builder::approximateDataSize);
            } else if (line.startsWith(LINE_OPEN_FILE_DESCRIPTOR_COUNT)) {
              parse(line, LINE_OPEN_FILE_DESCRIPTOR_COUNT, builder::openFileDescriptorCount);
            } else if (line.startsWith(LINE_MAX_FILE_DESCRIPTOR_COUNT)) {
              parse(line, LINE_MAX_FILE_DESCRIPTOR_COUNT, builder::maxFileDescriptorCount);
            }
          } catch (Exception e) {
            log.error("zookeeper four letter command('mntr') parsing error", e);
          }
        });

    return builder.build();
  }

}
