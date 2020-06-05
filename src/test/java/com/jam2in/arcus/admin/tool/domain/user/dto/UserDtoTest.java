package com.jam2in.arcus.admin.tool.domain.user.dto;

import com.jam2in.arcus.admin.tool.domain.user.entity.RoleEntity;
import com.jam2in.arcus.admin.tool.domain.user.entity.UserEntity;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public class UserDtoTest {

  @Test
  public void builder() {
    // given
    long id = 1L;
    String username = "foo";
    String email = "foo@bar.com";
    String password = "baz";
    String newPassword = "qux";
    Collection<String> roles = List.of(
        RoleEntity.ROLE_ADMIN.name(), RoleEntity.ROLE_USER.name());

    // when
    UserDto userDto = UserDto.builder()
        .id(id)
        .username(username)
        .email(email)
        .password(password)
        .newPassword(newPassword)
        .roles(roles)
        .build();

    // then
    assertThat(userDto.getId(), is(id));
    assertThat(userDto.getUsername(), is(username));
    assertThat(userDto.getEmail(), is(email));
    assertThat(userDto.getPassword(), is(password));
    assertThat(userDto.getNewPassword(), is(newPassword));
    assertThat(userDto.getRoles(), is(roles));
  }

  @Test
  public void of() {
    // given
    UserEntity userEntity = UserEntity.builder()
        .username("foo")
        .email("foo@bar.com")
        .password("baz")
        .roles(List.of(RoleEntity.ROLE_ADMIN, RoleEntity.ROLE_USER))
        .build();

    ReflectionTestUtils.setField(userEntity, "id", 1L);

    // when
    UserDto userDto = UserDto.of(userEntity);

    // then
    assertThat(userDto.getId(), is(userEntity.getId()));
    assertThat(userDto.getUsername(), is(userEntity.getUsername()));
    assertThat(userDto.getEmail(), is(userEntity.getEmail()));
    assertThat(userDto.getPassword(), is(nullValue()));
    assertThat(userDto.getNewPassword(), is(nullValue()));
    assertThat(userDto.getRoles(), is(userEntity.getRoles().stream()
        .map(Enum::name).collect(Collectors.toList())));
  }

  @Test
  public void of_list() {
    // given
    UserEntity userEntity1 = UserEntity.builder()
        .username("foo")
        .email("foo@bar.com")
        .password("baz")
        .roles(List.of(RoleEntity.ROLE_ADMIN))
        .build();
    ReflectionTestUtils.setField(userEntity1, "id", 1L);

    UserEntity userEntity2 = UserEntity.builder()
        .username("foofoo")
        .email("foofoo@barbar.com")
        .password("bazbaz")
        .roles(List.of(RoleEntity.ROLE_USER))
        .build();
    ReflectionTestUtils.setField(userEntity2, "id", 2L);

    // when
    List<UserDto> userDtos = UserDto.of(List.of(userEntity1, userEntity2));

    // then
    assertThat(userDtos, contains(
        allOf(
            hasProperty("username", is(userEntity1.getUsername())),
            hasProperty("email", is(userEntity1.getEmail())),
            hasProperty("password", is(nullValue())),
            hasProperty("roles", is(userEntity1.getRoles().stream()
                .map(Enum::name).collect(Collectors.toList())))
        ),
        allOf(
            hasProperty("username", is(userEntity2.getUsername())),
            hasProperty("email", is(userEntity2.getEmail())),
            hasProperty("password", is(nullValue())),
            hasProperty("roles", is(userEntity2.getRoles().stream()
                .map(Enum::name).collect(Collectors.toList())))
        )
    ));
  }

}
