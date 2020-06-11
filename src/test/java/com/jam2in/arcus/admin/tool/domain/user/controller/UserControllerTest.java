package com.jam2in.arcus.admin.tool.domain.user.controller;

import com.jam2in.arcus.admin.tool.domain.common.BaseControllerTest;
import com.jam2in.arcus.admin.tool.domain.user.dto.UserDto;
import com.jam2in.arcus.admin.tool.domain.user.dto.UserDtoUtils;
import com.jam2in.arcus.admin.tool.domain.user.service.UserService;
import com.jam2in.arcus.admin.tool.exception.ApiErrorCode;
import com.jam2in.arcus.admin.tool.util.PathUtils;
import org.apache.commons.lang3.StringUtils;
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
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasEntry;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
@WithMockUser(username = "test")
public class UserControllerTest extends BaseControllerTest {

  private static final String URL = "/api/v1/users";

  @MockBean
  public UserService userService;

  private UserDto.UserDtoBuilder userDtoBuilder;

  @Before
  public void before() {
    userDtoBuilder = UserDtoUtils.createBuilder();
  }

  @Test
  public void invalidContentSizeMin() throws Exception {
    // given
    UserDto userDto = userDtoBuilder
        .username(StringUtils.repeat('u', UserDto.SIZE_MIN_USERNAME - 1))
        .password(StringUtils.repeat('p', UserDto.SIZE_MIN_PASSWORD - 1))
        .newPassword(StringUtils.repeat('n', UserDto.SIZE_MIN_PASSWORD - 1))
        .build();

    // when
    ResultActions resultActions = post(URL, userDto);

    // then
    resultActions
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.code").value(ApiErrorCode.COMMON_INVALID_CONTENT.code()))
        .andExpect(jsonPath("$.message").value(ApiErrorCode.COMMON_INVALID_CONTENT.message()))
        .andExpect(jsonPath("$.details", containsInAnyOrder(
            allOf(
                hasEntry("name", "username"),
                hasEntry("value", userDto.getUsername()),
                hasEntry("reason", String.format("size must be between %d and %d",
                    UserDto.SIZE_MIN_USERNAME, UserDto.SIZE_MAX_USERNAME))),
            allOf(
                hasEntry("name", "password"),
                hasEntry("value", userDto.getPassword()),
                hasEntry("reason", String.format("size must be between %d and %d",
                    UserDto.SIZE_MIN_PASSWORD, UserDto.SIZE_MAX_PASSWORD))),
            allOf(
                hasEntry("name", "newPassword"),
                hasEntry("value", userDto.getNewPassword()),
                hasEntry("reason", String.format("size must be between %d and %d",
                    UserDto.SIZE_MIN_PASSWORD, UserDto.SIZE_MAX_PASSWORD)))
            ))).andDo(print());
  }

  @Test
  public void invalidContentSizeMax() throws Exception {
    // given
    UserDto userDto = userDtoBuilder
        .username(StringUtils.repeat('u', UserDto.SIZE_MAX_USERNAME + 1))
        .password(StringUtils.repeat('p', UserDto.SIZE_MAX_PASSWORD + 1))
        .newPassword(StringUtils.repeat('n', UserDto.SIZE_MAX_PASSWORD + 1))
        .build();

    // when
    ResultActions resultActions = post(URL, userDto);

    // then
    resultActions
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.code").value(ApiErrorCode.COMMON_INVALID_CONTENT.code()))
        .andExpect(jsonPath("$.message").value(ApiErrorCode.COMMON_INVALID_CONTENT.message()))
        .andExpect(jsonPath("$.details", containsInAnyOrder(
            allOf(
                hasEntry("name", "username"),
                hasEntry("value", userDto.getUsername()),
                hasEntry("reason", String.format("size must be between %d and %d",
                    UserDto.SIZE_MIN_USERNAME, UserDto.SIZE_MAX_USERNAME))),
            allOf(
                hasEntry("name", "password"),
                hasEntry("value", userDto.getPassword()),
                hasEntry("reason", String.format("size must be between %d and %d",
                    UserDto.SIZE_MIN_PASSWORD, UserDto.SIZE_MAX_PASSWORD))),
            allOf(
                hasEntry("name", "newPassword"),
                hasEntry("value", userDto.getNewPassword()),
                hasEntry("reason", String.format("size must be between %d and %d",
                    UserDto.SIZE_MIN_PASSWORD, UserDto.SIZE_MAX_PASSWORD)))
        ))).andDo(print());
  }

  @Test
  public void invalidContentEmail() throws Exception {
    // given
    UserDto userDto = userDtoBuilder
        .email("e")
        .build();

    // when
    ResultActions resultActions = post(URL, userDto);

    // then
    resultActions
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.code").value(ApiErrorCode.COMMON_INVALID_CONTENT.code()))
        .andExpect(jsonPath("$.message").value(ApiErrorCode.COMMON_INVALID_CONTENT.message()))
        .andExpect(jsonPath("$.details", containsInAnyOrder(
            allOf(
                hasEntry("name", "email"),
                hasEntry("value", userDto.getEmail()),
                hasEntry("reason", "must be a well-formed email address")))
        )).andDo(print());
  }

  @Test
  public void invalidContentNotEmpty() throws Exception {
    // given
    UserDto userDto = userDtoBuilder
        .username(null)
        .email(null)
        .password(null)
        .build();

    // when
    ResultActions resultActions = post(URL, userDto);

    // then
    resultActions
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.code").value(ApiErrorCode.COMMON_INVALID_CONTENT.code()))
        .andExpect(jsonPath("$.message").value(ApiErrorCode.COMMON_INVALID_CONTENT.message()))
        .andExpect(jsonPath("$.details", containsInAnyOrder(
            allOf(
                hasEntry("name", "username"),
                hasEntry("value", StringUtils.EMPTY),
                hasEntry("reason", "must not be empty")
            ),
            allOf(
                hasEntry("name", "email"),
                hasEntry("value", StringUtils.EMPTY),
                hasEntry("reason", "must not be empty")
            ),
            allOf(
                hasEntry("name", "password"),
                hasEntry("value", StringUtils.EMPTY),
                hasEntry("reason", "must not be empty")
            )
        ))).andDo(print());
  }

  @Test
  public void create() throws Exception {
    // given
    UserDto userDto = userDtoBuilder.build();

    given(userService.create(any())).willReturn(userDto);

    // when
    ResultActions resultActions = post(URL, userDto);

    // then
    resultActions
        .andExpect(status().isCreated())
        .andExpect((content().contentType(MediaType.APPLICATION_JSON)))
        .andExpect(content().json(objectMapper.writeValueAsString(userDto)))
        .andDo(print());
  }

  @Test
  public void create_invalidContent() throws Throwable {
    invalidContentNotEmpty(
        () -> post(URL, userDtoBuilder
            .username(null)
            .build()),
        "username"
    );
  }

  @Test
  public void update() throws Exception {
    // given
    UserDto userDto = userDtoBuilder.build();

    given(userService.update(anyLong(), any())).willReturn(userDto);

    // when
    ResultActions resultActions = put(URL + "/" + userDto.getId(), userDto);

    // then
    resultActions
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().json(objectMapper.writeValueAsString(userDto)))
        .andDo(print());
  }

  @Test
  public void update_invalidContent() throws Throwable {
    invalidContentNotEmpty(
        () -> put(PathUtils.create(URL, 1L), userDtoBuilder
            .username(null)
            .build()),
        "username"
    );
  }

  @Test
  public void get() throws Exception {
    // given
    UserDto userDto = userDtoBuilder.build();

    given(userService.get(userDto.getId())).willReturn(userDto);

    // when
    ResultActions resultActions = get(URL + "/" + userDto.getId());

    // then
    resultActions
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().json(objectMapper.writeValueAsString(userDto)))
        .andDo(print());
  }

  @Test
  public void getAll() throws Exception {
    // given
    UserDto userDto1 = userDtoBuilder
            .id(1L)
            .username(StringUtils.repeat('u', UserDto.SIZE_MIN_USERNAME))
            .password(StringUtils.repeat('p', UserDto.SIZE_MIN_PASSWORD))
            .email("foo@bar.com")
            .build();

    UserDto userDto2 = userDtoBuilder
            .id(2L)
            .username(StringUtils.repeat('x', UserDto.SIZE_MIN_USERNAME))
            .password(StringUtils.repeat('y', UserDto.SIZE_MIN_PASSWORD))
            .email("foo@bar.com")
            .build();

    given(userService.getAll()).willReturn(List.of(userDto1, userDto2));

    // when
    ResultActions resultActions = get(URL);

    // then
    resultActions
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().json("["
            + objectMapper.writeValueAsString(userDto1)
            + ","
            + objectMapper.writeValueAsString(userDto2)
            + "]"))
        .andDo(print());
  }

  @Test
  public void delete() throws Exception {
    // given
    UserDto userDto = userDtoBuilder.build();

    // when
    ResultActions resultActions = delete(URL + "/" + userDto.getId());

    // then
    resultActions
        .andExpect(status().isOk())
        .andDo(print());
  }

}
