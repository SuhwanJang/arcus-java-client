package com.jam2in.arcus.admin.tool.domain.user.service;

import com.jam2in.arcus.admin.tool.domain.user.dto.UserDto;
import com.jam2in.arcus.admin.tool.domain.user.entity.RoleEntity;
import com.jam2in.arcus.admin.tool.domain.user.entity.UserEntity;
import com.jam2in.arcus.admin.tool.exception.ApiErrorCode;
import com.jam2in.arcus.admin.tool.exception.BusinessException;
import com.jam2in.arcus.admin.tool.domain.user.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
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

  @Test
  public void create() {
    // given
    String encodedPassword = "bar";
    UserDto userDto = UserDto.builder().password("foo").build();

    given(passwordEncoder.encode(userDto.getPassword())).willReturn(encodedPassword);
    given(userRepository.existsByUsername(userDto.getUsername())).willReturn(false);
    given(userRepository.existsByEmail(userDto.getEmail())).willReturn(false);

    // when
    userService.create(userDto);

    // then
    ArgumentCaptor<UserEntity> userEntityCaptor = ArgumentCaptor.forClass(UserEntity.class);
    verify(userRepository, atMostOnce()).save(userEntityCaptor.capture());
    assertThat(userEntityCaptor.getValue().getPassword(), is(encodedPassword));
  }

  @Test
  public void create_duplicateUsername() {
    // given
    UserDto userDto = UserDto.builder().username("foo").build();

    given(userRepository.existsByUsername(userDto.getUsername())).willReturn(true);

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
  public void create_duplicateEmail() {
    // given
    UserDto userDto = UserDto.builder().email("foo").build();

    given(userRepository.existsByEmail(userDto.getEmail())).willReturn(true);

    try {
      // when
      userService.create(userDto);
    } catch (BusinessException e) {
      // then
      verify(userRepository, never()).save(any());
      assertThat(e.getApiError().getCode(), is(ApiErrorCode.USER_EMAIL_DUPLICATED.code()));
      return;
    }

    fail();
  }

  @Test
  public void update() {
    // given
    String encodedPassword = "foobar";

    UserDto userDto = UserDto.builder()
        .id(1L)
        .username("foo")
        .email("bar")
        .password("baz")
        .newPassword("qux")
        .roles(List.of(RoleEntity.ROLE_ADMIN.name()))
        .build();

    UserEntity userEntity = UserEntity.builder()
        .username(userDto.getUsername() + " u")
        .email(userDto.getEmail() + " e")
        .password(userDto.getPassword())
        .roles(List.of(RoleEntity.ROLE_ADMIN))
        .build();

    given(userRepository.findById(userDto.getId()))
        .willReturn(Optional.of(userEntity));
    given(passwordEncoder.encode(userDto.getNewPassword()))
        .willReturn(encodedPassword);
    given(passwordEncoder.matches(userEntity.getPassword(), userDto.getPassword()))
        .willReturn(true);

    // when
    userService.update(userDto.getId(), userDto);

    // then
    assertThat(userEntity.getUsername(), is(not(userDto.getUsername())));
    assertThat(userEntity.getEmail(), is(userDto.getEmail()));
    assertThat(userEntity.getPassword(), is(encodedPassword));
  }

  @Test
  public void update_notFound() {
    // given
    UserDto userDto = UserDto.builder()
        .id(1L)
        .username("foo")
        .email("bar")
        .password("baz")
        .newPassword("qux")
        .roles(List.of(RoleEntity.ROLE_ADMIN.name()))
        .build();

    UserEntity userEntity = UserEntity.builder()
        .username(userDto.getUsername() + " u")
        .email(userDto.getEmail() + " e")
        .password(userDto.getPassword())
        .roles(List.of(RoleEntity.ROLE_ADMIN))
        .build();

    given(userRepository.findById(userDto.getId()))
        .willReturn(Optional.empty());

    try {
      // when
      userService.update(userDto.getId(), userDto);
    } catch (BusinessException e) {
      // then
      assertThat(e.getApiError().getCode(), is(ApiErrorCode.USER_NOT_FOUND.code()));
      assertThat(userEntity.getUsername(), is(not(userDto.getUsername())));
      assertThat(userEntity.getEmail(), is(not(userDto.getEmail())));
      assertThat(userEntity.getPassword(), is(userDto.getPassword()));
      return;
    }

    fail();
  }

  @Test
  public void update_mismatchPassword() {
    // given
    String encodedPassword = "foobar";

    UserDto userDto = UserDto.builder()
        .id(1L)
        .username("foo")
        .email("bar")
        .password("baz")
        .newPassword("qux")
        .roles(List.of(RoleEntity.ROLE_ADMIN.name()))
        .build();

    UserEntity userEntity = UserEntity.builder()
        .username(userDto.getUsername() + " u")
        .email(userDto.getEmail() + " e")
        .password(userDto.getPassword())
        .roles(List.of(RoleEntity.ROLE_ADMIN))
        .build();

    given(userRepository.findById(userDto.getId()))
        .willReturn(Optional.of(userEntity));
    given(passwordEncoder.matches(userEntity.getPassword(), userDto.getPassword()))
        .willReturn(false);

    try {
      // when
      userService.update(userDto.getId(), userDto);
    } catch (BusinessException e) {
      // then
      assertThat(e.getApiError().getCode(), is(ApiErrorCode.USER_PASSWORD_MISMATCH.code()));
      assertThat(userEntity.getUsername(), is(not(userDto.getUsername())));
      assertThat(userEntity.getEmail(), is(not(userDto.getEmail())));
      assertThat(userEntity.getPassword(), is(not(encodedPassword)));
      return;
    }

    fail();
  }

  @Test
  public void update_duplicateEmail() {
    // given
    String encodedPassword = "foobar";

    UserDto userDto = UserDto.builder()
        .id(1L)
        .username("foo")
        .email("bar")
        .password("baz")
        .newPassword("qux")
        .roles(List.of(RoleEntity.ROLE_ADMIN.name()))
        .build();

    UserEntity userEntity = UserEntity.builder()
        .username(userDto.getUsername() + " u")
        .email(userDto.getEmail() + " e")
        .password(userDto.getPassword())
        .roles(List.of(RoleEntity.ROLE_ADMIN))
        .build();

    given(userRepository.findById(userDto.getId()))
        .willReturn(Optional.of(userEntity));
    given(userRepository.existsByEmail(userDto.getEmail()))
        .willReturn(true);
    given(passwordEncoder.matches(userEntity.getPassword(), userDto.getPassword()))
        .willReturn(true);

    try {
      // when
      userService.update(userDto.getId(), userDto);
    } catch (BusinessException e) {
      // then
      assertThat(e.getApiError().getCode(), is(ApiErrorCode.USER_EMAIL_DUPLICATED.code()));
      assertThat(userEntity.getUsername(), is(not(userDto.getUsername())));
      assertThat(userEntity.getEmail(), is(not(userDto.getEmail())));
      assertThat(userEntity.getPassword(), is(not(encodedPassword)));
      return;
    }

    fail();
  }

  @Test
  public void get() {
    // given
    long id = 1L;

    given(userRepository.findById(id))
        .willReturn(Optional.of(UserEntity.builder()
            .roles(List.of(RoleEntity.ROLE_ADMIN))
            .build()));

    // when
    UserDto userDto = userService.get(id);

    // then
    verify(userRepository, atMostOnce()).findById(id);
    assertThat(userDto, is(notNullValue()));
  }

  @Test
  public void get_notFound() {
    // given
    long id = 1L;

    given(userRepository.findById(id)).willReturn(Optional.empty());

    try {
      // when
      userService.get(id);
    } catch (BusinessException e) {
      // then
      assertThat(e.getApiError().getCode(), is(ApiErrorCode.USER_NOT_FOUND.code()));
      return;
    }

    fail();
  }

  @Test
  public void getByUsername() {
    // given
    String username = "foo";

    given(userRepository.findByUsername(username))
        .willReturn(Optional.of(UserEntity.builder()
            .roles(List.of(RoleEntity.ROLE_ADMIN))
            .build()));

    // when
    UserDto userDto = userService.getByUsername(username);

    // then
    verify(userRepository, atMostOnce()).findByUsername(username);
    assertThat(userDto, is(notNullValue()));
  }

  @Test
  public void getByUsername_notFound() {
    // given
    String username = "foo";

    given(userRepository.findByUsername(username)).willReturn(Optional.empty());

    try {
      // when
      userService.getByUsername(username);
    } catch (BusinessException e) {
      // then
      assertThat(e.getApiError().getCode(), is(ApiErrorCode.USER_USERNAME_NOT_FOUND.code()));
      return;
    }

    fail();
  }

  @Test
  public void getAll() {
    // given
    UserDto userDto = UserDto.builder()
        .id(1L)
        .username("foo")
        .email("bar")
        .password("baz")
        .newPassword("qux")
        .roles(List.of(RoleEntity.ROLE_ADMIN.name()))
        .build();

    UserEntity userEntity1 = UserEntity.builder()
        .username(userDto.getUsername() + " u")
        .email(userDto.getEmail() + " e")
        .password(userDto.getPassword())
        .roles(List.of(RoleEntity.ROLE_ADMIN))
        .build();

    UserEntity userEntity2 = UserEntity.builder()
        .username(userDto.getUsername() + " d")
        .email(userDto.getEmail() + " v")
        .password(userDto.getPassword())
        .roles(List.of(RoleEntity.ROLE_USER))
        .build();

    given(userRepository.findAll()).willReturn(List.of(userEntity1, userEntity2));

    // when
    List<UserDto> userDtos = userService.getAll();

    // then
    verify(userRepository, atMostOnce()).findAll();
    assertThat(userDtos.size(), is(2));
  }

  @Test
  public void getAll_noUser() {
    // given
    given(userRepository.findAll()).willReturn(List.of());

    try {
      // when
      userService.getAll();
    } catch (BusinessException e) {
      // then
      assertThat(e.getApiError().getCode(), is(ApiErrorCode.NO_USER.code()));
      return;
    }

    fail();
  }

  @Test
  public void delete() {
    // given
    long id = 1L;

    given(userRepository.existsById(id)).willReturn(true);

    // when
    userService.delete(id);

    // then
    verify(userRepository, atMostOnce()).deleteById(id);
  }

  @Test
  public void delete_notFound() {
    // given
    long id = 1L;

    given(userRepository.existsById(id)).willReturn(false);

    try {
      // when
      userService.delete(id);
    } catch (BusinessException e) {
      // then
      assertThat(e.getApiError().getCode(), is(ApiErrorCode.USER_NOT_FOUND.code()));
      return;
    }

    fail();
  }

}
