package com.jam2in.arcus.admin.tool.domain.ensemble.parser;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ZooKeeperFourLetterRuokParser {

  public static String parse(String ruok) {
    return StringUtils.replace(ruok, "\n", "");
  }

}
