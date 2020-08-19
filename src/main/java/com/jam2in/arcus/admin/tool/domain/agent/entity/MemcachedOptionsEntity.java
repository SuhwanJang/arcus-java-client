package com.jam2in.arcus.admin.tool.domain.agent.entity;

import com.jam2in.arcus.admin.tool.domain.agent.dto.MemcachedOptionsDto;
import com.jam2in.arcus.admin.tool.util.ModelMapperUtils;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "mcoptions")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MemcachedOptionsEntity {

  @Id
  private String address;

  @Column(nullable = false)
  private Integer memlimit;

  @Column(nullable = false)
  private Integer connections;

  @Column(nullable = false)
  private Integer threads;

  @Column(nullable = false)
  private String zookeeper;

  @Builder
  public MemcachedOptionsEntity(String address,
                                Integer memlimit,
                                Integer connections,
                                Integer threads,
                                String zookeeper) {
    this.address = address;
    this.memlimit = memlimit;
    this.connections = connections;
    this.threads = threads;
    this.zookeeper = zookeeper;
  }

  public static MemcachedOptionsEntity of(MemcachedOptionsDto memcachedOptionsDto,
                                          String address) {
    MemcachedOptionsEntityBuilder builder = ModelMapperUtils.map(
        memcachedOptionsDto, MemcachedOptionsEntityBuilder.class);
    builder.address = address;
    return builder.build();
  }

}
