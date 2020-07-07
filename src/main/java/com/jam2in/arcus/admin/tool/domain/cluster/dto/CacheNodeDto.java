package com.jam2in.arcus.admin.tool.domain.cluster.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CacheNodeDto {

  public CacheNodeDto(String address) {
    this.address = address;
  }

  @NotEmpty
  private String address;

}
