package com.jam2in.arcus.admin.tool.domain.kubernetes.entity;

import com.jam2in.arcus.admin.tool.domain.common.entity.DateEntity;
import com.jam2in.arcus.admin.tool.domain.kubernetes.dto.KubernetesDto;
import com.jam2in.arcus.admin.tool.util.ModelMapperUtils;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "kubernetes")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class KubernetesEntity extends DateEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String name;

  @Column(nullable = false, unique = true)
  private String address;

  @Column(nullable = false)
  private String token;

  @Builder
  public KubernetesEntity(String name,
                          String address,
                          String token) {
    this.name = name;
    this.address = address;
    this.token = token;
  }

  public void updateName(String name) {
    this.name = name;
  }

  public void updateAddress(String address) {
    this.address = address;
  }

  public void updateToken(String token) {
    this.token = token;
  }

  public static KubernetesEntity of(KubernetesDto kubernetesDto) {
    return ModelMapperUtils.map(kubernetesDto, KubernetesEntity.class);
  }

}
