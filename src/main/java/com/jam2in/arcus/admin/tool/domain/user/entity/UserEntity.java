package com.jam2in.arcus.admin.tool.domain.user.entity;

import com.jam2in.arcus.admin.tool.domain.common.entity.DateEntity;
import com.jam2in.arcus.admin.tool.domain.user.dto.UserDto;
import com.jam2in.arcus.admin.tool.domain.user.type.Access;
import com.jam2in.arcus.admin.tool.domain.user.type.Role;
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
import java.util.List;

@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserEntity extends DateEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String username;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  private Role role;

  @Builder
  public UserEntity(String username,
                    String email,
                    String password,
                    Role role,
                    List<Access> accesses) {
    this.username = username;
    this.email = email;
    this.password = password;
    this.role = role;
    this.accesses = accesses;
  }

  @ElementCollection
  @JoinTable (
      name = "accesses",
      joinColumns = {
        @JoinColumn(name = "id")
      }
  )
  @Enumerated(EnumType.STRING)
  @Column(name = "access")
  private List<Access> accesses;

  public void updateUsername(String username) {
    this.username = username;
  }

  public void updateEmail(String email) {
    this.email = email;
  }

  public void updatePassword(String encodedPassword) {
    password = encodedPassword;
  }

  public void applyAdminRole() {
    role = Role.ROLE_ADMIN;
  }

  public void applyUserRole() {
    role = Role.ROLE_USER;
  }

  public void updateRole(Role role) {
    this.role = role;
  }

  public void updateAccesses(List<Access> accesses) {
    this.accesses = accesses;
  }

  public static UserEntity of(UserDto userDto) {
    return ModelMapperUtils.map(
        userDto, UserEntityBuilder.class).build();
  }

}
