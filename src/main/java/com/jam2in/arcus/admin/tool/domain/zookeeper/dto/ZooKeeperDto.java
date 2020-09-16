package com.jam2in.arcus.admin.tool.domain.zookeeper.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jam2in.arcus.admin.tool.domain.common.validator.Port;
import com.jam2in.arcus.admin.tool.domain.zookeeper.entity.ZooKeeperEntity;
import com.jam2in.arcus.admin.tool.domain.zookeeper.validator.MyId;
import com.jam2in.arcus.admin.tool.util.ModelMapperUtils;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ZooKeeperDto {

  private Long id;

  @MyId
  @Setter
  private Integer myId;

  @NotEmpty
  private String ip;

  @Port
  @NotNull
  private Integer clientPort;

  @Port
  @NotNull
  private Integer serverPort;

  @Port
  @NotNull
  private Integer electionPort;

  @Setter
  private ZooKeeperFourLetterDto stats;

  @Builder
  public ZooKeeperDto(Long id,
                      Integer myId,
                      String ip,
                      Integer clientPort,
                      Integer serverPort,
                      Integer electionPort) {
    this.id = id;
    this.myId = myId;
    this.ip = ip;
    this.clientPort = clientPort;
    this.serverPort = serverPort;
    this.electionPort = electionPort;
  }

  @JsonIgnore
  public String getAddress() {
    return ip + ":" + clientPort;
  }

  public static ZooKeeperDto of(ZooKeeperEntity zookeeperEntity) {
    return ModelMapperUtils.map(zookeeperEntity, ZooKeeperDto.class);
  }

  public static List<ZooKeeperDto> of(List<ZooKeeperEntity> zookeeperEntities) {
    return zookeeperEntities
        .stream()
        .collect(
            ArrayList::new,
            (zookeeperDtos, zookeeperEntity) -> zookeeperDtos.add(of(zookeeperEntity)),
            List::addAll);
  }

  public static final int MYID_MIN = 0;

}
