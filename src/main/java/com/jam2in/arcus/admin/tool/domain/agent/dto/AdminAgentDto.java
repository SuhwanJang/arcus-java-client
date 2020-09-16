package com.jam2in.arcus.admin.tool.domain.agent.dto;

import com.jam2in.arcus.admin.tool.domain.agent.entity.AdminAgentEntity;
import com.jam2in.arcus.admin.tool.domain.common.validator.AddressValidator;
import com.jam2in.arcus.admin.tool.domain.common.validator.Port;
import com.jam2in.arcus.admin.tool.util.ModelMapperUtils;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class AdminAgentDto {

  private Long id;

  @NotEmpty
  private String ip;

  @NotNull
  @Port
  private Integer port;

  @NotEmpty
  private String token;

  @Builder
  public AdminAgentDto(Long id,
                       String ip,
                       Integer port,
                       String token) {
    this.id = id;
    this.ip = ip;
    this.port = port;
    this.token = token;
  }

  public static AdminAgentDto of(AdminAgentEntity adminAgentEntity) {
    return ModelMapperUtils.map(adminAgentEntity, AdminAgentDto.class);
  }

}
