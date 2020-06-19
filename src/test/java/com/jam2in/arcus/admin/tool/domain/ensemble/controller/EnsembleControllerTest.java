package com.jam2in.arcus.admin.tool.domain.ensemble.controller;

import com.jam2in.arcus.admin.tool.domain.common.BaseControllerTest;
import com.jam2in.arcus.admin.tool.domain.ensemble.dto.EnsembleDto;
import com.jam2in.arcus.admin.tool.domain.ensemble.dto.EnsembleDtoUtils;
import com.jam2in.arcus.admin.tool.domain.ensemble.service.EnsembleService;
import com.jam2in.arcus.admin.tool.error.ApiErrorCode;
import com.jam2in.arcus.admin.tool.util.PathUtils;
import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasEntry;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(EnsembleController.class)
@WithMockUser(username = "test")
public class EnsembleControllerTest extends BaseControllerTest {

  /*
  private static final String URL = "/api/v1/ensembles";

  @MockBean
  public EnsembleService ensembleService;

  private EnsembleDto.EnsembleDtoBuilder ensembleDtoBuilder;

  @Before
  public void before() {
    ensembleDtoBuilder = EnsembleDtoUtils.createBuilder();
  }

  @Test
  public void invalidContentSizeMin() throws Exception {
    // given
    EnsembleDto ensembleDto = ensembleDtoBuilder
        .name(StringUtils.repeat('n', EnsembleDto.SIZE_MIN_NAME - 1))
        .build();

    // when
    ResultActions resultActions = post(URL, ensembleDto);

    // then
    resultActions
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.code").value(ApiErrorCode.COMMON_INVALID_CONTENT.code()))
        .andExpect(jsonPath("$.message").value(ApiErrorCode.COMMON_INVALID_CONTENT.message()))
        .andExpect(jsonPath("$.details", Matchers.contains(
          allOf(
              hasEntry("name", "name"),
              hasEntry("value", ensembleDto.getName()),
              hasEntry("reason", String.format("size must be between %d and %d",
                  EnsembleDto.SIZE_MIN_NAME, EnsembleDto.SIZE_MAX_NAME))
          )
        ))).andDo(print());
  }

  @Test
  public void invalidContentSizeMax() throws Exception {
    // given
    EnsembleDto ensembleDto = ensembleDtoBuilder
        .name(StringUtils.repeat('n', EnsembleDto.SIZE_MAX_NAME + 1))
        .build();

    // when
    ResultActions resultActions = post(URL, ensembleDto);

    // then
    resultActions
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.code").value(ApiErrorCode.COMMON_INVALID_CONTENT.code()))
        .andExpect(jsonPath("$.message").value(ApiErrorCode.COMMON_INVALID_CONTENT.message()))
        .andExpect(jsonPath("$.details", Matchers.contains(
          allOf(
              hasEntry("name", "name"),
              hasEntry("value", ensembleDto.getName()),
              hasEntry("reason", String.format("size must be between %d and %d",
                  EnsembleDto.SIZE_MIN_NAME, EnsembleDto.SIZE_MAX_NAME))
          )
        ))).andDo(print());
  }

  @Test
  public void invalidContentNotEmpty() throws Throwable {
    invalidContentNotEmpty(
        () -> post(URL, ensembleDtoBuilder
            .name(null)
            .build()),
        "name"
    );
  }

  @Test
  public void create() throws Exception {
    // given
    EnsembleDto ensembleDto = ensembleDtoBuilder.build();

    given(ensembleService.create(any())).willReturn(ensembleDto);

    // when
    ResultActions resultActions = post(URL, ensembleDto);

    // then
    resultActions
        .andExpect(status().isCreated())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().json(objectMapper.writeValueAsString(ensembleDto)))
        .andDo(print());
  }

  @Test
  public void create_invalidContent() throws Throwable {
    invalidContentNotEmpty(
        () -> post(URL, ensembleDtoBuilder
            .name(null)
            .build()),
        "name"
    );
  }

  @Test
  public void update() throws Exception {
    // given
    EnsembleDto ensembleDto = ensembleDtoBuilder.build();

    given(ensembleService.update(anyLong(), any(EnsembleDto.class))).willReturn(ensembleDto);

    // when
    ResultActions resultActions = put(PathUtils.create(URL, ensembleDto.getId()), ensembleDto);

    // then
    resultActions
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().json(objectMapper.writeValueAsString(ensembleDto)))
        .andDo(print());
  }

  @Test
  public void update_invalidContent() throws Throwable {
    invalidContentNotEmpty(
        () -> put(PathUtils.create(URL, 1L), ensembleDtoBuilder
        .name(null)
        .build()),
        "name"
    );
  }

  @Test
  public void get() throws Exception {
    // given
    EnsembleDto ensembleDto = ensembleDtoBuilder.build();

    given(ensembleService.get(ensembleDto.getId())).willReturn(ensembleDto);

    // when
    ResultActions resultActions = get(PathUtils.create(URL, ensembleDto.getId()));

    // then
    resultActions
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().json(objectMapper.writeValueAsString(ensembleDto)))
        .andDo(print());
  }

  @Test
  public void getAll() throws Exception {
    // given
    EnsembleDto ensembleDto1 = ensembleDtoBuilder
        .id(1L)
        .name(StringUtils.repeat('n', EnsembleDto.SIZE_MIN_NAME))
        .zookeepers(List.of(
            "192.168.0.1:2181",
            "192.168.0.2:2181",
            "192.168.0.3:2181"
        )).build();

    EnsembleDto ensembleDto2 = ensembleDtoBuilder
        .id(2L)
        .name(StringUtils.repeat('a', EnsembleDto.SIZE_MIN_NAME))
        .zookeepers(List.of(
            "192.168.10.1:2181",
            "192.168.10.2:2181",
            "192.168.10.3:2181"
        )).build();

    given(ensembleService.getAll()).willReturn(List.of(ensembleDto1, ensembleDto2));

    // when
    ResultActions resultActions = get(URL);

    // then
    resultActions
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().json("["
            + objectMapper.writeValueAsString(ensembleDto1)
            + ","
            + objectMapper.writeValueAsString(ensembleDto2)
            + "]"
        ))
        .andDo(print());
  }

  @Test
  public void delete() throws Exception {
    // given
    EnsembleDto ensembleDto = ensembleDtoBuilder.build();

    // when
    ResultActions resultActions = delete(PathUtils.create(URL, ensembleDto.getId()));

    // then
    resultActions
        .andExpect(status().isOk())
        .andDo(print());
  }
   */

}
