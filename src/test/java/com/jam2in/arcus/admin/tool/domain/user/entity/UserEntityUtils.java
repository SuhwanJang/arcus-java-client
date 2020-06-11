package com.jam2in.arcus.admin.tool.domain.user.entity;

import com.jam2in.arcus.admin.tool.domain.user.dto.UserDto;
import com.jam2in.arcus.admin.tool.domain.user.dto.UserDtoUtils;
import com.jam2in.arcus.admin.tool.domain.user.type.Access;
import com.jam2in.arcus.admin.tool.domain.user.type.Role;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserEntityUtils {

  public static UserEntity.UserEntityBuilder createBuilder() {
    UserDto userDto = UserDtoUtils.createBuilder().build();

    return UserEntity.builder()
        .username(userDto.getUsername() + "u")
        .email(userDto.getEmail() + "e")
        .password(userDto.getPassword())
        .role(Role.ROLE_USER)
        .accesses(List.of(
            Access.ACCESS_CACHE_ITEM_MANAGEMENT,
            Access.ACCESS_CACHE_DIAGNOSIS_MANAGEMENT));
  }

  public static UserEntity.UserEntityBuilder createBuilder(UserDto userDto) {
    return UserEntity.builder()
        .username(userDto.getUsername() + "u")
        .email(userDto.getEmail() + "e")
        .password(userDto.getPassword())
        .role(Role.ROLE_USER)
        .accesses(List.of(
            Access.ACCESS_CACHE_ITEM_MANAGEMENT,
            Access.ACCESS_CACHE_DIAGNOSIS_MANAGEMENT));
  }

  public static void equals(UserEntity userEntity,
                            String username,
                            String email,
                            String password,
                            Role role,
                            Collection<Access> accesses) {
    assertThat(userEntity.getId(), is(nullValue()));
    assertThat(userEntity.getUsername(), is(username));
    assertThat(userEntity.getEmail(), is(email));
    assertThat(userEntity.getPassword(), is(password));
    assertThat(userEntity.getRole(), is(role));
    assertThat(userEntity.getAccesses(), is(accesses));
  }

  public static void equals(UserEntity userEntity, UserDto userDto) {
    assertThat(userEntity.getId(), is(nullValue()));
    assertThat(userEntity.getUsername(), is(userDto.getUsername()));
    assertThat(userEntity.getEmail(), is(userDto.getEmail()));
    assertThat(userEntity.getPassword(), is(userDto.getPassword()));
    assertThat(userEntity.getRole(), is(userDto.getRole()));
    assertThat(userEntity.getAccesses(), is(userDto.getAccesses()));
  }

  public static void equals(UserEntity userEntity, UserDto userDto, String encodedPassword) {
    assertThat(userEntity.getId(), is(userDto.getId()));
    assertThat(userEntity.getUsername(), is(userDto.getUsername()));
    assertThat(userEntity.getEmail(), is(userDto.getEmail()));
    assertThat(userEntity.getPassword(), is(encodedPassword));
    assertThat(userEntity.getRole(), is(userDto.getRole()));
    assertThat(userEntity.getAccesses(), is(userDto.getAccesses()));
  }

}
