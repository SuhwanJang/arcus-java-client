package com.jam2in.arcus.admin.tool.domain.cache.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CacheClusterBaseDto {

  protected CacheClusterBaseDto(String serviceCode) {
    this.serviceCode = serviceCode;
  }

  @NotEmpty
  protected String serviceCode;

}
