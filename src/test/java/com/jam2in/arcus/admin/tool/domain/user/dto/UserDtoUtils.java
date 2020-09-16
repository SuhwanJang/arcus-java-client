package com.jam2in.arcus.admin.tool.domain.user.dto;

import com.jam2in.arcus.admin.tool.domain.user.entity.UserEntity;
import com.jam2in.arcus.admin.tool.domain.user.type.Access;
import com.jam2in.arcus.admin.tool.domain.user.type.Role;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserDtoUtils {

  public static UserDto.UserDtoBuilder createBuilder() {
    return UserDto.builder()
      .id(1L)
      .username(StringUtils.repeat('u', UserDto.USERNAME_SIZE_MIN))
      .email("foo@bar.com")
      .password(StringUtils.repeat('p', UserDto.PASSWORD_SIZE_MIN))
      .newPassword(StringUtils.repeat('n', UserDto.PASSWORD_SIZE_MIN))
      .role(Role.ROLE_ADMIN)
      .accesses(List.of(
          Access.ACCESS_ZOOKEEPER_CLUSTER_MANAGEMENT,
          Access.ACCESS_CACHE_CLUSTER_MANAGEMENT));
  }

  public static void equals(UserDto userDto,
                            Long id,
                            String username,
                            String email,
                            String password,
                            String newPassword,
                            Role role,
                            List<Access> accesses) {
    assertThat(userDto.getId(), is(id));
    assertThat(userDto.getUsername(), is(username));
    assertThat(userDto.getEmail(), is(email));
    assertThat(userDto.getPassword(), is(password));
    assertThat(userDto.getNewPassword(), is(newPassword));
    assertThat(userDto.getRole(), is(role));
    assertThat(userDto.getAccesses(), is(accesses));
  }

  public static void equals(UserDto userDto, UserEntity userEntity) {
    assertThat(userDto.getId(), is(userEntity.getId()));
    assertThat(userDto.getUsername(), is(userEntity.getUsername()));
    assertThat(userDto.getEmail(), is(userEntity.getEmail()));
    assertThat(userDto.getPassword(), is(nullValue()));
    assertThat(userDto.getNewPassword(), is(nullValue()));
    assertThat(userDto.getRole(), is(userEntity.getRole()));
    assertThat(userDto.getAccesses(), is(userEntity.getAccesses()));
  }

}
