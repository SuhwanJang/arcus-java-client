package com.jam2in.arcus.admin.tool.domain.ensemble.controller;

import com.jam2in.arcus.admin.tool.domain.common.BaseControllerTest;
import com.jam2in.arcus.admin.tool.domain.ensemble.dto.EnsembleDto;
import com.jam2in.arcus.admin.tool.domain.ensemble.service.EnsembleService;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@WebMvcTest(EnsembleController.class)
@WithMockUser(username = "test")
public class EnsembleControllerTest extends BaseControllerTest {

  private static final String URL = "/api/v1/ensembles";

  @MockBean
  public EnsembleService ensembleService;

  private EnsembleDto.EnsembleDtoBuilder ensembleDtoBuilder;

  @Before
  public void before() {
    ensembleDtoBuilder = EnsembleDto.builder()
        .id(1L)
        .name("foo")
        .zookeepers(List.of(
           "192.168.0.1:2181",
           "192.168.0.2:2181",
           "192.168.0.3:2181"));
  }

  // TODO: test

}
