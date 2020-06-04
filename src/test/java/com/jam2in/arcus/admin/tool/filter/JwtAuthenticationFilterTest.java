package com.jam2in.arcus.admin.tool.filter;

import com.jam2in.arcus.admin.tool.bean.JwtTokenProvider;
import com.jam2in.arcus.admin.tool.domain.common.controller.BaseControllerTest;
import com.jam2in.arcus.admin.tool.domain.common.controller.TestController;
import com.jam2in.arcus.admin.tool.domain.user.dto.UserDto;
import com.jam2in.arcus.admin.tool.domain.user.entity.UserEntity;
import com.jam2in.arcus.admin.tool.domain.user.service.TestService;
import com.jam2in.arcus.admin.tool.exception.ApiErrorCode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(TestController.class)
public class JwtAuthenticationFilterTest extends BaseControllerTest {

  private static final String URL = "/login";

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private JwtTokenProvider jwtTokenProvider;

  @MockBean
  private TestService testService;

  @Test
  public void successfulAuthentication() throws Exception {
    // given
    UserDto userDto = UserDto.builder()
        .username("foofoo")
        .password("barbar").build();

    UserEntity userEntity = UserEntity.of(userDto);
    userEntity.applyAdminRole();
    userEntity.updatePassword(passwordEncoder.encode(userDto.getPassword()));

    given(userRepository.findByUsername(userDto.getUsername()))
        .willReturn(Optional.of(userEntity));

    // when
    ResultActions resultActions = post(URL, userDto);

    // then
    resultActions
        .andExpect(status().isOk())
        .andDo(print());

    assertThat(
        jwtTokenProvider.validate(
        jwtTokenProvider.resolve(
            resultActions.andReturn()
                .getResponse()
                .getHeader(HttpHeaders.AUTHORIZATION))),
        is(true));
  }

  @Test
  public void unsuccessfulAuthentication() throws Exception {
    // given
    UserDto userDto = UserDto.builder()
        .username("foofoo")
        .password("barbar").build();

    // when
    ResultActions resultActions = post(URL, userDto);

    // then
    resultActions
        .andExpect(status().isUnauthorized())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.code").value(
            ApiErrorCode.COMMON_INVALID_USERNAME_OR_PASSWORD.code()))
        .andExpect(jsonPath("$.message").value(
            ApiErrorCode.COMMON_INVALID_USERNAME_OR_PASSWORD.message()))
        .andExpect(jsonPath("$.details", is(empty())))
        .andDo(print());
  }

}
