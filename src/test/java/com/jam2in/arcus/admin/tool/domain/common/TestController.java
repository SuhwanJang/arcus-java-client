package com.jam2in.arcus.admin.tool.domain.common;

import com.jam2in.arcus.admin.tool.domain.user.entity.TestDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/test")
public class TestController {

  private final TestService testService;

  public TestController(TestService testService) {
    this.testService = testService;
  }

  @GetMapping
  @ResponseStatus(code = HttpStatus.OK)
  public void get() throws Exception {
    testService.test();
  }

  @PostMapping
  @ResponseStatus(code = HttpStatus.OK)
  public void post(@RequestBody @Valid TestDto testDto) throws Exception {
    testService.test();
  }

}
