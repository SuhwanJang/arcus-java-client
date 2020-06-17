package com.jam2in.arcus.admin.tool.domain.ensemble.parser;

import com.jam2in.arcus.admin.tool.domain.ensemble.dto.ZooKeeperFourLetterConsDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public final class ZooKeeperFourLetterConsParser {

  /*
    /127.0.0.1:39220[0](queued=0,recved=1,sent=0)"
    /0:0:0:0:0:0:0:1:36765[0](queued=0,recved=1,sent=0)
   */

  private static final String LINE_QUEUED = "queued=";
  private static final String LINE_RECEIVED = "recved=";
  private static final String LINE_SENT = "sent=";

  public static void parseAddress(
      ZooKeeperFourLetterConsDto.ZooKeeperFourLetterConsDtoBuilder builder,
      String line) {
    builder.address(StringUtils.trim(StringUtils.substringBefore(line, "(").replace("/", "")));
  }

  public static void parseCount(
      ZooKeeperFourLetterConsDto.ZooKeeperFourLetterConsDtoBuilder builder,
      String line) {
    String count = StringUtils.substringAfter(line, "(").replace(")", "");
    String[] splitted = count.split(",");

    builder.queued(StringUtils.substringAfter(splitted[0], LINE_QUEUED));
    builder.received(StringUtils.substringAfter(splitted[1], LINE_RECEIVED));
    builder.sent(StringUtils.substringAfter(splitted[2], LINE_SENT));
  }


  public static Collection<ZooKeeperFourLetterConsDto> parse(String cons) {
    return Stream.of(cons.split("\n"))
        .collect(
            ArrayList::new,
            (dtos, line) -> {
              try {
                ZooKeeperFourLetterConsDto.ZooKeeperFourLetterConsDtoBuilder builder =
                    ZooKeeperFourLetterConsDto.builder();

                parseAddress(builder, line);
                parseCount(builder, line);

                dtos.add(builder.build());
              } catch (Exception e) {
                log.error("zookeeper four letter command('cons') parsing error", e);
              }
            },
            List::addAll
        );
  }

}
