package com.jam2in.arcus.admin.tool.domain.user.service;

import com.jam2in.arcus.admin.tool.domain.user.dto.UserDto;
import com.jam2in.arcus.admin.tool.domain.user.entity.RoleEntity;
import com.jam2in.arcus.admin.tool.domain.user.entity.UserEntity;
import com.jam2in.arcus.admin.tool.exception.ApiError;
import com.jam2in.arcus.admin.tool.exception.ApiErrorCode;
import com.jam2in.arcus.admin.tool.exception.BusinessException;
import com.jam2in.arcus.admin.tool.domain.user.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.powermock.api.mockito.PowerMockito;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

  @InjectMocks
  private UserService userService;

  @Mock
  private UserRepository userRepository;

  @Mock
  private PasswordEncoder passwordEncoder;

  private static final String ENCODED_PASSWORD = "bar";

  @Test
  public void create() {
    // given
    UserDto userDto = UserDto.builder().password("foo").build();
    ArgumentCaptor<UserEntity> userEntityCapture = ArgumentCaptor.forClass(UserEntity.class);

    given(passwordEncoder.encode(userDto.getPassword())).willReturn(ENCODED_PASSWORD);
    given(userRepository.exists(any())).willReturn(false);

    // when
    userService.create(userDto);

    // then
    verify(userRepository, atMostOnce()).save(userEntityCapture.capture());
    assertThat(userEntityCapture.getValue().getPassword(), is(ENCODED_PASSWORD));
  }

  @Test
  public void create_usernameExists() throws Exception {
    // given
    UserDto userDto = UserDto.builder().username("foo").build();

    PowerMockito.when(userService, "isExistsByUsername", userDto.getUsername())
        .thenReturn(true);

    try {
      // when
      userService.create(userDto);
    } catch (BusinessException e) {
      // then
      verify(userRepository, never()).save(any());
      assertThat(e.getApiError().getCode(), is(ApiErrorCode.USER_USERNAME_DUPLICATED.code()));
      return;
    }

    fail();
  }

  @Test
  public void create_emailExists() throws Exception {
    // given
    PowerMockito.when(userService, "isExistsByEmail", any())
        .thenReturn(true);

    try {
      // when
      userService.create(UserDto.builder().build());
    } catch (BusinessException e) {
      // then
      verify(userRepository, never()).save(any());
      assertThat(e.getApiError().getCode(), is(ApiErrorCode.USER_USERNAME_DUPLICATED.code()));
      return;
    }

    fail();
  }

  @Test
  public void get_notFound() {
    // given
    long userId = 1L;

    given(userRepository.findById(userId)).willReturn(Optional.empty());

    try {
      // when
      userService.get(userId);
    } catch (BusinessException e) {
      // then
      verify(userRepository, atMostOnce()).findById(userId);
      assertThat(e.getApiError().getCode(), is(ApiErrorCode.USER_NOT_FOUND.code()));
      return;
    }

    fail();
  }

  @Test
  public void getByUsername_notFound() {
    // given
    given(userRepository.findByUsername(any())).willReturn(Optional.empty());

    // when
    userService.getByUsername("foo");
  }

  @Test
  public void delete() {
    // given
    given(userRepository.existsById(any())).willReturn(true);

    // when
    userService.delete(1L);

    // then
    verify(userRepository, atMostOnce()).delete(any());
  }

  @Test
  public void delete_notFound() {
    // given
    given(userRepository.existsById(any())).willReturn(false);

    try {
      // when
      userService.delete(1L);
    } catch (BusinessException e) {
      // then
      verify(userRepository, never()).delete(any());
      assertThat(e.getApiError().getCode(), is(ApiErrorCode.USER_NOT_FOUND.code()));
      return;
    }

    fail();
  }

  @Test
  public void getUserDetails() {
    // given
    UserEntity userEntity =
        UserEntity.builder()
          .username("foo")
          .password("bar")
          .roles(List.of(RoleEntity.ROLE_ADMIN, RoleEntity.ROLE_USER))
          .build();

    User user = new User(
        userEntity.getUsername(),
        userEntity.getPassword(),
        userEntity.getRoles());

    given(userRepository.findByUsername(any())).willReturn(Optional.of(userEntity));

    // when
    UserDetails userDetails = userService.getUserDetails(userEntity.getUsername());

    // then
    assertThat(userDetails.getUsername(), is(user.getUsername()));
    assertThat(userDetails.getPassword(), is(user.getPassword()));
    assertThat(userDetails.getAuthorities(), is(user.getAuthorities()));
  }

  @Test
  public void getUserDetails_notFound() {
    // given
    given(userRepository.findByUsername(any())).willReturn(Optional.empty());

    try {
      // when
      userService.getUserDetails("foo");
    } catch (BusinessException e) {
      // then
      assertThat(e.getApiError().getCode(), is(ApiErrorCode.USER_USERNAME_NOT_FOUND.code()));
      return;
    }

    fail();
  }

}
