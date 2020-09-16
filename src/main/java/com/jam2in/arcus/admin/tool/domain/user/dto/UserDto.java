package com.jam2in.arcus.admin.tool.domain.user.dto;

import com.jam2in.arcus.admin.tool.domain.common.dto.DateDto;
import com.jam2in.arcus.admin.tool.domain.user.entity.UserEntity;
import com.jam2in.arcus.admin.tool.domain.user.type.Access;
import com.jam2in.arcus.admin.tool.domain.user.type.Role;
import com.jam2in.arcus.admin.tool.util.ModelMapperUtils;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserDto extends DateDto {

  private Long id;

  @Size(min = USERNAME_SIZE_MIN, max = USERNAME_SIZE_MAX)
  @NotEmpty
  private String username;

  @Email
  @NotEmpty
  private String email;

  @Size(min = PASSWORD_SIZE_MIN, max = PASSWORD_SIZE_MAX)
  @NotEmpty
  private String password;

  @Size(min = PASSWORD_SIZE_MIN, max = PASSWORD_SIZE_MAX)
  private String newPassword;

  private Role role;

  private List<Access> accesses;

  @Builder
  public UserDto(Long id,
                 String username,
                 String email,
                 String password,
                 String newPassword,
                 Role role,
                 List<Access> accesses) {
    this.id = id;
    this.username = username;
    this.email = email;
    this.newPassword = newPassword;
    this.password = password;
    this.role = role;
    this.accesses = accesses;
  }

  public static UserDto of(UserEntity userEntity) {
    UserDto userDto = ModelMapperUtils.map(
        userEntity, UserDto.class);

    userDto.password = null;

    return userDto;
  }

  public static List<UserDto> of(List<UserEntity> userEntities) {
    return userEntities
        .stream()
        .collect(
            ArrayList::new,
            (userDtos, userEntity) -> userDtos.add(of(userEntity)),
            List::addAll);
  }

  public static final int USERNAME_SIZE_MIN = 4;
  public static final int USERNAME_SIZE_MAX = 32;

  public static final int PASSWORD_SIZE_MIN = 8;
  public static final int PASSWORD_SIZE_MAX = 64;

}
