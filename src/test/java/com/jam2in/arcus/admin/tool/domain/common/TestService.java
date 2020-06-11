package com.jam2in.arcus.admin.tool.domain.common;

import org.springframework.stereotype.Service;

@Service
public class TestService {

  public TestDto test() throws Exception {
    return new TestDto();
  }

}
