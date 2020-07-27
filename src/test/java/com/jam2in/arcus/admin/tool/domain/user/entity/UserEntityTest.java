package com.jam2in.arcus.admin.tool.domain.user.entity;

import com.jam2in.arcus.admin.tool.domain.user.dto.UserDto;
import com.jam2in.arcus.admin.tool.domain.user.dto.UserDtoUtils;
import com.jam2in.arcus.admin.tool.domain.user.type.Access;
import com.jam2in.arcus.admin.tool.domain.user.type.Role;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class UserEntityTest {

  @Test
  public void builder() {
    // given
    String username = "foo";
    String email = "foo@bar.com";
    String password = "baz";
    Role role = Role.ROLE_ADMIN;
    List<Access> accesses = List.of(
        Access.ACCESS_CACHE_CLUSTER_MANAGEMENT,
        Access.ACCESS_ZOOKEEPER_CLUSTER_MANAGEMENT);

    // when
    UserEntity userEntity = UserEntity.builder()
        .username(username)
        .email(email)
        .password(password)
        .role(role)
        .accesses(accesses)
        .build();

    // then
    UserEntityUtils.equals(userEntity,
        username, email, password, role, accesses);
  }

  @Test
  public void of() {
    // given
    UserDto userDto = UserDtoUtils.createBuilder().build();

    // when
    UserEntity userEntity = UserEntity.of(userDto);

    // then
    UserEntityUtils.equals(userEntity, userDto);
  }

  @Test
  public void updateEmail() {
    // given
    UserEntity userEntity = UserEntity.builder()
        .email("foo")
        .build();

    // when
    userEntity.updateEmail("bar");

    // then
    assertThat(userEntity.getEmail(), is("bar"));
  }

  @Test
  public void updatePassword() {
    // given
    UserEntity userEntity = UserEntity.builder()
        .password("foo")
        .build();

    // when
    userEntity.updatePassword("bar");

    // then
    assertThat(userEntity.getPassword(), is("bar"));
  }

}
