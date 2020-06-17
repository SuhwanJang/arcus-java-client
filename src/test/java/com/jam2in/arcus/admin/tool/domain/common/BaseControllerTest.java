package com.jam2in.arcus.admin.tool.domain.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jam2in.arcus.admin.tool.domain.user.repository.UserRepository;
import com.jam2in.arcus.admin.tool.error.ApiErrorCode;
import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.function.ThrowingSupplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasEntry;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BaseControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  protected ObjectMapper objectMapper;

  @MockBean
  protected UserRepository userRepository;

  protected ResultActions post(String url, String content) throws Exception {
    return mockMvc.perform(
      MockMvcRequestBuilders.post(url)
        .content(content)
        .contentType(MediaType.APPLICATION_JSON))
        .andDo(print());
  }

  protected ResultActions post(String url, Object content) throws Exception {
    return post(url, objectMapper.writeValueAsString(content));
  }

  protected ResultActions put(String url, String content) throws Exception {
    return mockMvc.perform(
        MockMvcRequestBuilders.put(url)
          .content(content)
          .contentType(MediaType.APPLICATION_JSON))
          .andDo(print());
  }

  protected ResultActions put(String url, Object content) throws Exception {
    return put(url, objectMapper.writeValueAsString(content));
  }

  protected ResultActions get(String url) throws Exception {
    return mockMvc.perform(
        MockMvcRequestBuilders.get(url))
        .andDo(print());
  }

  protected ResultActions delete(String url) throws Exception {
    return mockMvc.perform(
        MockMvcRequestBuilders.delete(url))
        .andDo(print());
  }

  protected void invalidContentNotEmpty(ThrowingSupplier<ResultActions> supplier,
                                        String name) throws Throwable {
    // when
    ResultActions resultActions = supplier.get();

    // then
    resultActions
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.code").value(ApiErrorCode.COMMON_INVALID_CONTENT.code()))
        .andExpect(jsonPath("$.message").value(ApiErrorCode.COMMON_INVALID_CONTENT.message()))
        .andExpect(jsonPath("$.details", Matchers.contains(
          allOf(
              hasEntry("name", name),
              hasEntry("value", StringUtils.EMPTY),
              hasEntry("reason", "must not be empty")
          )
        ))).andDo(print());
  }

}
