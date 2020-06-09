package com.jam2in.arcus.admin.tool.domain.user.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "admins")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class AdminEntity {

  @Builder
  public AdminEntity(UserEntity userEntity) {
    this.user = userEntity;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne
  private UserEntity user;

  @Column(unique = true)
  private final String restriction = "restriction";

}
