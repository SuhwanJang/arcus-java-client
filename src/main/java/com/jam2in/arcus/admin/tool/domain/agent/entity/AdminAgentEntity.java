package com.jam2in.arcus.admin.tool.domain.agent.entity;

import com.jam2in.arcus.admin.tool.domain.agent.dto.AdminAgentDto;
import com.jam2in.arcus.admin.tool.util.ModelMapperUtils;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "agents")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class AdminAgentEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String ip;

  @Column(nullable = false)
  @Setter
  private Integer port;

  @Column(nullable = false)
  @Setter
  private String token;

  @Builder
  public AdminAgentEntity(String ip,
                          Integer port,
                          String token) {
    this.ip = ip;
    this.port = port;
    this.token = token;
  }

  public String getAddress() {
    return ip + ":" + port;
  }

  public static AdminAgentEntity of(AdminAgentDto adminAgentDto) {
    return ModelMapperUtils.map(adminAgentDto, AdminAgentEntity.class);
  }

}
