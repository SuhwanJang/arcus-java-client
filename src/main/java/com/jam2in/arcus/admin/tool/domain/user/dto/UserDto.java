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
import java.util.Collection;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserDto extends DateDto {

  @Builder
  public UserDto(Long id,
                 String username,
                 String email,
                 String password,
                 String newPassword,
                 Role role,
                 Collection<Access> accesses) {
    this.id = id;
    this.username = username;
    this.email = email;
    this.newPassword = newPassword;
    this.password = password;
    this.role = role;
    this.accesses = accesses;
  }

  private Long id;

  @Size(min = SIZE_MIN_USERNAME, max = SIZE_MAX_USERNAME)
  @NotEmpty
  private String username;

  @Email
  @NotEmpty
  private String email;

  @Size(min = SIZE_MIN_PASSWORD, max = SIZE_MAX_PASSWORD)
  @NotEmpty
  private String password;

  @Size(min = SIZE_MIN_PASSWORD, max = SIZE_MAX_PASSWORD)
  private String newPassword;

  private Role role;

  private Collection<Access> accesses;

  public static UserDto of(UserEntity userEntity) {
    UserDto userDto = ModelMapperUtils.map(
        userEntity, UserDto.class);

    userDto.password = null;

    return userDto;
  }

  public static Collection<UserDto> of(Collection<UserEntity> userEntities) {
    return userEntities
        .stream()
        .collect(
            ArrayList::new,
            (userDtos, userEntity) -> userDtos.add(of(userEntity)),
            List::addAll);
  }

  public static final int SIZE_MIN_USERNAME = 4;
  public static final int SIZE_MAX_USERNAME = 32;

  public static final int SIZE_MIN_PASSWORD = 8;
  public static final int SIZE_MAX_PASSWORD = 64;

}
