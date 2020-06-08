package com.jam2in.arcus.admin.tool.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PathUtils {

  public static String create(Object... paths) {
    return StringUtils.join(paths, "/");
  }

}
