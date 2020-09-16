package com.jam2in.arcus.admin.tool.domain.zookeeper.dto;

import com.jam2in.arcus.admin.tool.domain.zookeeper.validator.MyId;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ZooKeeperParticipantsDto {

  @MyId
  @NotNull
  private Integer myId;

  @NotEmpty
  private String zkservers;

  @Builder
  public ZooKeeperParticipantsDto(Integer myId,
                                  String zkservers) {
    this.myId = myId;
    this.zkservers = zkservers;
  }

  public static String generateParticipants(List<ZooKeeperDto> zooKeeperDtos) {
    return zooKeeperDtos
        .stream()
        .map(zooKeeperDto ->
            String.format("server.%d:%s:%d:%d:participant:%d",
                zooKeeperDto.getMyId(),
                zooKeeperDto.getIp(),
                zooKeeperDto.getServerPort(),
                zooKeeperDto.getElectionPort(),
                zooKeeperDto.getClientPort()))
        .collect(Collectors.joining("\n"));
  }

}
