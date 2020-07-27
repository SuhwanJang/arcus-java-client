package com.jam2in.arcus.admin.tool.domain.kubernetes.dto;

import com.jam2in.arcus.admin.tool.domain.kubernetes.entity.KubernetesEntity;
import com.jam2in.arcus.admin.tool.util.ModelMapperUtils;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class KubernetesDto {

  private Long id;

  @Size(min = SIZE_MIN_NAME, max = SIZE_MAX_NAME)
  @NotEmpty
  private String name;

  @NotEmpty
  private String address;

  @NotEmpty
  private String token;

  @Builder
  public KubernetesDto(Long id,
                       String name,
                       String address,
                       String token) {
    this.id = id;
    this.name = name;
    this.address = address;
    this.token = token;
  }

  public static KubernetesDto of(KubernetesEntity kubernetesEntity) {
    return ModelMapperUtils.map(kubernetesEntity, KubernetesDto.class);
  }

  public static final int SIZE_MIN_NAME = 4;
  public static final int SIZE_MAX_NAME = 32;

}
