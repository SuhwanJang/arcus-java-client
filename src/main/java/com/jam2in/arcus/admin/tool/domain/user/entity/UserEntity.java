package com.jam2in.arcus.admin.tool.domain.user.entity;

import com.jam2in.arcus.admin.tool.domain.user.dto.UserDto;
import com.jam2in.arcus.admin.tool.util.ModelMapperUtils;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Table;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserEntity {

  @Builder
  public UserEntity(String username,
                    String email,
                    String password,
                    Collection<RoleEntity> roles) {
    this.username = username;
    this.email = email;
    this.password = password;
    this.roles = roles;
    this.registered = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String username;

  // FIXME: 제거
  @Column(nullable = false)
  private String registered;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false)
  private String password;

  @ElementCollection
  @JoinTable (
      name = "roles",
      joinColumns = {
        @JoinColumn(name = "id")
      }
  )
  @Enumerated(EnumType.STRING)
  @Column(name = "role")
  private Collection<RoleEntity> roles;

  public void updateEmail(String email) {
    this.email = email;
  }

  public void updatePassword(String encodedPassword) {
    password = encodedPassword;
  }

  public void applyAdminRole() {
    roles = List.of(RoleEntity.ROLE_ADMIN);
  }

  public void applyUserRole() {
    roles = List.of(RoleEntity.ROLE_USER);
  }

  public static UserEntity of(UserDto userDto) {
    return ModelMapperUtils.map(
        userDto, UserEntityBuilder.class).build();
  }

}
