package com.jam2in.arcus.admin.tool.domain.user.service;

import com.jam2in.arcus.admin.tool.domain.user.dto.UserDtoUtils;
import com.jam2in.arcus.admin.tool.domain.user.entity.UserEntityUtils;
import com.jam2in.arcus.admin.tool.domain.user.dto.UserDto;
import com.jam2in.arcus.admin.tool.domain.user.entity.UserEntity;
import com.jam2in.arcus.admin.tool.error.ApiErrorCode;
import com.jam2in.arcus.admin.tool.exception.BusinessException;
import com.jam2in.arcus.admin.tool.domain.user.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
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

  private UserDto userDto;

  private UserEntity userEntity;

  private String encodedPassword;

  @Before
  public void before() {
    userDto = UserDtoUtils.createBuilder().build();
    userEntity = UserEntityUtils.createBuilder(userDto).build();
    encodedPassword = userDto.getNewPassword() + "e";

    ReflectionTestUtils.setField(userEntity, "id", userDto.getId());
  }

  @Test
  public void create() {
    // given
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
    given(userRepository.findById(userDto.getId()))
        .willReturn(Optional.of(userEntity));
    given(passwordEncoder.encode(userDto.getNewPassword()))
        .willReturn(encodedPassword);
    given(passwordEncoder.matches(userEntity.getPassword(), userDto.getPassword()))
        .willReturn(true);

    // when
    userService.update(userDto.getId(), userDto);

    // then
    UserEntityUtils.equals(userEntity, userDto, encodedPassword);
  }

  @Test
  public void update_notFound() {
    // given
    given(userRepository.findById(userDto.getId()))
        .willReturn(Optional.empty());

    try {
      // when
      userService.update(userDto.getId(), userDto);
    } catch (BusinessException e) {
      // then
      assertThat(e.getApiError().getCode(), is(ApiErrorCode.USER_NOT_FOUND.code()));
      return;
    }

    fail();
  }

  @Test
  public void update_mismatchPassword() {
    // given
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
      return;
    }

    fail();
  }

  @Test
  public void update_duplicateUsername() {
    // given
    given(userRepository.findById(userDto.getId()))
        .willReturn(Optional.of(userEntity));
    given(userRepository.existsByUsername(userDto.getUsername()))
        .willReturn(true);
    given(passwordEncoder.matches(userEntity.getPassword(), userDto.getPassword()))
        .willReturn(true);

    try {
      // when
      userService.update(userDto.getId(), userDto);
    } catch (BusinessException e) {
      // then
      assertThat(e.getApiError().getCode(), is(ApiErrorCode.USER_USERNAME_DUPLICATED.code()));
      return;
    }

    fail();
  }


  @Test
  public void update_duplicateEmail() {
    // given
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
      return;
    }

    fail();
  }

  @Test
  public void get() {
    // given
    long id = 1L;

    given(userRepository.findById(id))
        .willReturn(Optional.of(userEntity));

    // when
    UserDto userDto = userService.get(id);

    // then
    verify(userRepository, atMostOnce()).findById(id);
    UserDtoUtils.equals(userDto, userEntity);
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
        .willReturn(Optional.of(userEntity));

    // when
    UserDto userDto = userService.getByUsername(username);

    // then
    verify(userRepository, atMostOnce()).findByUsername(username);
    UserDtoUtils.equals(userDto, userEntity);
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
    UserEntity userEntity1 = UserEntity.builder()
        .username("foo")
        .build();
    ReflectionTestUtils.setField(userEntity1, "id", 1L);

    UserEntity userEntity2 = UserEntity.builder()
        .username("foofoo")
        .build();
    ReflectionTestUtils.setField(userEntity2, "id", 2L);

    given(userRepository.findAll()).willReturn(List.of(userEntity1, userEntity2));

    // when
    List<UserDto> userDtos = (List<UserDto>) userService.getAll();

    // then
    verify(userRepository, atMostOnce()).findAll();
    UserDtoUtils.equals(userDtos.get(0), userEntity1);
    UserDtoUtils.equals(userDtos.get(1), userEntity2);
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
