package com.jam2in.arcus.admin.tool.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class StringUtils {

  public static String substringAfterLast(String str, String seperator) {
    String substring = org.apache.commons.lang3.StringUtils.substringAfterLast(str, seperator);

    if (org.apache.commons.lang3.StringUtils.isEmpty(substring)) {
      substring = str;
    }

    return substring;
  }

}
