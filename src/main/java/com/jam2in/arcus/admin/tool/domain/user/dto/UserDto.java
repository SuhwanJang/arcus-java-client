package com.jam2in.arcus.admin.tool.domain.user.dto;

import com.jam2in.arcus.admin.tool.domain.user.entity.RoleEntity;
import com.jam2in.arcus.admin.tool.domain.user.entity.UserEntity;
import com.jam2in.arcus.admin.tool.util.ModelMapperUtil;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserDto {

  @Builder
  public UserDto(Long id,
                 String username,
                 String email,
                 String registered,
                 String password,
                 String newPassword,
                 Collection<String> roles) {
    this.id = id;
    this.username = username;
    this.email = email;
    this.registered = registered;
    this.password = password;
    this.newPassword = newPassword;
    this.roles = roles;
  }

  private Long id;

  @Size(min = 4, max = 32)
  @NotEmpty
  private String username;

  @Email
  @NotEmpty
  private String email;

  private String registered;

  @Size(min = 8, max = 64)
  @NotEmpty
  private String password;

  @Size(min = 8, max = 64)
  private String newPassword;

  private Collection<String> roles;

  public static UserDto of(UserEntity userEntity) {
    UserDto userDto = ModelMapperUtil.map(
        userEntity, UserDto.class);

    userDto.password = null;

    userDto.roles = userEntity.getRoles()
        .stream().map(Enum::name)
        .collect(Collectors.toList());

    return userDto;
  }

  public static List<UserDto> of(List<UserEntity> userEntities) {
    return userEntities.stream().collect(
        ArrayList::new,
        (userDtos, userEntity) -> userDtos.add(of(userEntity)),
        List::addAll);
  }
}
