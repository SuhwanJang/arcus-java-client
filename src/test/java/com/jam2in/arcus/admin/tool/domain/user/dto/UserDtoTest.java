package com.jam2in.arcus.admin.tool.domain.user.dto;

import com.jam2in.arcus.admin.tool.domain.user.entity.UserEntity;
import com.jam2in.arcus.admin.tool.domain.user.entity.UserEntityUtils;
import com.jam2in.arcus.admin.tool.domain.user.type.Access;
import com.jam2in.arcus.admin.tool.domain.user.type.Role;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

public class UserDtoTest {

  @Test
  public void builder() {
    // given
    long id = 1L;
    String username = "foo";
    String email = "foo@bar.com";
    String password = "baz";
    String newPassword = "qux";
    Role role = Role.ROLE_ADMIN;
    List<Access> accesses = List.of(
        Access.ACCESS_CACHE_CLUSTER_MANAGEMENT,
        Access.ACCESS_ZOOKEEPER_CLUSTER_MANAGEMENT);

    // when
    UserDto userDto = UserDto.builder()
        .id(id)
        .username(username)
        .email(email)
        .password(password)
        .newPassword(newPassword)
        .role(role)
        .accesses(accesses)
        .build();

    // then
    UserDtoUtils.equals(userDto,
        id, username, email, password, newPassword, role, accesses);
  }

  @Test
  public void of() {
    // given
    UserEntity userEntity = UserEntityUtils.createBuilder().build();

    ReflectionTestUtils.setField(userEntity, "id", 1L);

    // when
    UserDto userDto = UserDto.of(userEntity);

    // then
    UserDtoUtils.equals(userDto, userEntity);
  }

  @Test
  public void of_list() {
    // given
    UserEntity userEntity1 = UserEntity.builder()
        .username("foo")
        .build();
    ReflectionTestUtils.setField(userEntity1, "id", 1L);

    UserEntity userEntity2 = UserEntity.builder()
        .username("foofoo")
        .build();
    ReflectionTestUtils.setField(userEntity2, "id", 2L);

    // when
    List<UserDto> userDtos = (List<UserDto>) UserDto.of(List.of(userEntity1, userEntity2));

    // then
    UserDtoUtils.equals(userDtos.get(0), userEntity1);
    UserDtoUtils.equals(userDtos.get(1), userEntity2);
  }

}
