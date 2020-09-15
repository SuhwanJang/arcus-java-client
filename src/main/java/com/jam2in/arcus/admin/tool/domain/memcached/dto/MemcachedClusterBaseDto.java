package com.jam2in.arcus.admin.tool.domain.memcached.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MemcachedClusterBaseDto {

  @NotEmpty
  protected String serviceCode;

  protected MemcachedClusterBaseDto(String serviceCode) {
    this.serviceCode = serviceCode;
  }

}
