package com.jam2in.arcus.admin.tool.domain.agent.dto;

import com.jam2in.arcus.admin.tool.domain.agent.entity.AgentEntity;
import com.jam2in.arcus.admin.tool.util.ModelMapperUtils;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class AgentDto {

  private Long id;

  @NotEmpty
  private String ip;

  @NotEmpty
  @Size(min = SIZE_MIN_PORT, max = SIZE_MAX_PORT)
  private Integer port;

  @NotEmpty
  private String token;

  @Builder
  public AgentDto(Long id,
                  String ip,
                  Integer port,
                  String token) {
    this.id = id;
    this.ip = ip;
    this.port = port;
    this.token = token;
  }

  public static AgentDto of(AgentEntity agentEntity) {
    return ModelMapperUtils.map(agentEntity, AgentDto.class);
  }

  public static final int SIZE_MIN_PORT = 1;
  public static final int SIZE_MAX_PORT = 65535;

}
