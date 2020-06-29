package com.jam2in.arcus.admin.tool.util;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class PathUtilsTest {

  @Test
  public void getPath() {
    String base = "/api/v1/test";
    String relative1 = "foo";
    String relative2 = "bar";

    assertThat(PathUtils.path(base), is("/api/v1/test"));
    assertThat(PathUtils.path(base, relative1), is("/api/v1/test/foo"));
    assertThat(PathUtils.path(base, relative1, relative2), is("/api/v1/test/foo/bar"));
  }

}
