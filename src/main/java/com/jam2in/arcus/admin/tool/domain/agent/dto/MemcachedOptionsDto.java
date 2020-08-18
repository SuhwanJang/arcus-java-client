package com.jam2in.arcus.admin.tool.domain.agent.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MemcachedOptionsDto {

  @NotNull
  @Min(1)
  private Integer memlimit;

  @NotNull
  @Min(1)
  private Integer connections;

  @NotNull
  @Min(1)
  private Integer threads;

  @NotEmpty
  private String zookeeper;

  @Builder
  public MemcachedOptionsDto(Integer memlimit,
                             Integer connections,
                             Integer threads,
                             String zookeeper) {
    this.memlimit = memlimit;
    this.connections = connections;
    this.threads = threads;
    this.zookeeper = zookeeper;
  }

}
