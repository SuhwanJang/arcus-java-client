package com.jam2in.arcus.admin.tool.domain.user.service;

import com.jam2in.arcus.admin.tool.domain.user.entity.TestDto;
import org.springframework.stereotype.Service;

@Service
public class TestService {

  public TestDto test() throws Exception {
    return new TestDto();
  }

}
