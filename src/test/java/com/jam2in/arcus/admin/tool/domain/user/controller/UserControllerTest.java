package com.jam2in.arcus.admin.tool.domain.user.controller;

import com.jam2in.arcus.admin.tool.bean.UserPasswordEncoder;
import com.jam2in.arcus.admin.tool.domain.user.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

// TODO: more test
//@SpringBootTest
@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class) // Controller, ControllerAdvice만 자동 설정됨
//@AutoConfigureMockMvc  // Controller, Service, Resource, Repository, Component 모든 컨텍스트를 함께 올림
//@Transactional
public class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UserService userService;

  @MockBean
  private UserPasswordEncoder userPasswordEncoder;

  @Test
  public void create() {
  }


  /*
  @Test
  public void create() throws Exception {
    // given
    UserDto userDto = UserDto.builder()
        .username("foo")
        .password("foo")
        .email("foo@bar.com")
        .build();

    // when
    ResultActions resultActions = mockMvc.perform(
        post("/api/v1/users")
            .content(objectMapper.writeValueAsString(userDto))
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print());

    // then
    resultActions
        .andExpect(status().isCreated())
        .andDo(print())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON));
  }
   */

}
