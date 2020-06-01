package com.jam2in.arcus.admin.tool.domain.user.service;

import com.jam2in.arcus.admin.tool.domain.user.dto.UserDto;
import com.jam2in.arcus.admin.tool.domain.user.entity.RoleEntity;
import com.jam2in.arcus.admin.tool.domain.user.entity.UserEntity;
import com.jam2in.arcus.admin.tool.exception.ApiErrorCode;
import com.jam2in.arcus.admin.tool.exception.BusinessException;
import com.jam2in.arcus.admin.tool.domain.user.repository.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.ReflectionUtils;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyObject;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

// TODO: more test
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
  public void create_passwordEncoded() throws BusinessException {
    // given
    ArgumentCaptor<UserEntity> userEntityCapture = ArgumentCaptor.forClass(UserEntity.class);
    given(passwordEncoder.encode(any())).willReturn(ENCODED_PASSWORD);
    given(userRepository.exists(any())).willReturn(false);

    // when
    userService.create(UserDto.builder().password("foo").build());

    // then
    verify(userRepository, atMostOnce()).save(userEntityCapture.capture());
    assertThat(userEntityCapture.getValue().getPassword(), is(ENCODED_PASSWORD));
  }

  @Test
  public void create_usernameExists() {
    // given
    UserDto userDto = UserDto.builder().username("foo").build();
    given(ReflectionTestUtils.invokeMethod(
        userService, "isExistsByUsername", userDto.getUsername()))
        .willReturn(true);

    // when
    try {
      userService.create(userDto);
    } catch (BusinessException e) {
      // then
      assertThat(e.getApiError().getCode(), is(ApiErrorCode.USER_USERNAME_DUPLICATED.code()));
      return;
    }

    Assert.fail("ff");
  }

  @Test(expected = BusinessException.class)
  public void create_emailExists() throws BusinessException {
    given(userRepository.exists(any())).willReturn(true);

    // when
    userService.create(UserDto.builder().build());
  }

  @Test(expected = BusinessException.class)
  public void get_notFound() throws BusinessException {
    // given
    given(userRepository.findById(any())).willReturn(Optional.empty());

    // when
    userService.get(1L);
  }

  @Test(expected = BusinessException.class)
  public void getByUsername_notFound() throws BusinessException {
    // given
    given(userRepository.findByUsername(any())).willReturn(Optional.empty());

    // when
    userService.getByUsername("foo");
  }

  @Test(expected = BusinessException.class)
  public void delete_notFound() throws BusinessException {
    // given
    given(userRepository.existsById(any())).willReturn(false);

    // when
    userService.delete(1L);
  }

  @Test
  public void getUserDetails() throws BusinessException {
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

  @Test(expected = BusinessException.class)
  public void getUserDetails_notFound() throws BusinessException {
    // given
    given(userRepository.findByUsername(any())).willReturn(Optional.empty());

    // when
    userService.getUserDetails("foo");

    // then
  }

}
