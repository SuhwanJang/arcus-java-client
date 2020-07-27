package com.jam2in.arcus.admin.tool.bean;

import com.jam2in.arcus.admin.tool.domain.user.dto.UserDto;
import org.apache.commons.collections4.ListUtils;
import org.springframework.security.core.userdetails.User;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UserPrincipal extends User {

  private final UserDto userDto;

  public UserPrincipal(UserDto userDto, String password) {
    super(userDto.getUsername(), password, Stream.concat(
        Stream.of(userDto.getRole()),
        ListUtils.emptyIfNull(userDto.getAccesses()).stream())
        .collect(Collectors.toList()));

    this.userDto = userDto;
  }

  public UserDto getUserDto() {
    return userDto;
  }

}
