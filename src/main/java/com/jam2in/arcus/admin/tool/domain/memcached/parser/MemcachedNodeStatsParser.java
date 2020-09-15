package com.jam2in.arcus.admin.tool.domain.memcached.parser;

import com.google.common.net.HostAndPort;
import com.jam2in.arcus.admin.tool.domain.memcached.dto.MemcachedNodeStatsDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public final class MemcachedNodeStatsParser {

  private static final String LINE_VERSION = "STAT version ";
  private static final String LINE_ZK_TIMEOUT = "STAT zk_timeout ";

  private static void parseVersion(
      MemcachedNodeStatsDto.CacheNodeStatsDtoBuilder builder,
      String line) {
    builder.version(StringUtils.substringAfter(line, LINE_VERSION));
  }

  private static void parseZkTimeout(
      MemcachedNodeStatsDto.CacheNodeStatsDtoBuilder builder,
      String line) {
    builder.zkTimeout(StringUtils.substringAfter(line, LINE_ZK_TIMEOUT));
  }

  @SuppressWarnings("UnstableApiUsage")
  public static MemcachedNodeStatsDto parse(String address, String stats) {
    MemcachedNodeStatsDto.CacheNodeStatsDtoBuilder builder =
        MemcachedNodeStatsDto.builder();

    for (String line : stats.split("\n")) {
      try {
        if (line.startsWith(LINE_VERSION)) {
          parseVersion(builder, line);
        } else if (line.startsWith(LINE_ZK_TIMEOUT)) {
          parseZkTimeout(builder, line);
          break;
        }
      } catch (Exception e) {
        log.error("cache node stats parsing error", e);
      }
    }

    HostAndPort hostAndPort = HostAndPort.fromString(address);
    return builder.host(hostAndPort.getHost()).port(Integer.toString(hostAndPort.getPort()))
        .build();
  }

}
