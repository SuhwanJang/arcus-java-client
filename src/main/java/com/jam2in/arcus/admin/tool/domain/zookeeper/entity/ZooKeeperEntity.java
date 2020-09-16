package com.jam2in.arcus.admin.tool.domain.zookeeper.entity;

import com.jam2in.arcus.admin.tool.domain.common.entity.DateEntity;
import com.jam2in.arcus.admin.tool.domain.zookeeper.dto.ZooKeeperDto;
import com.jam2in.arcus.admin.tool.util.ModelMapperUtils;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
    name = "zookeepers",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"ensembleId", "myId"}),
        @UniqueConstraint(columnNames = {"ensembleId", "ip", "clientPort"}),
        @UniqueConstraint(columnNames = {"ensembleId", "ip", "serverPort"}),
        @UniqueConstraint(columnNames = {"ensembleId", "ip", "electionPort"})
    }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ZooKeeperEntity extends DateEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private Integer myId;

  @Column(nullable = false)
  private String ip;

  @Column(nullable = false)
  private Integer clientPort;

  @Column(nullable = false)
  private Integer serverPort;

  @Column(nullable = false)
  private Integer electionPort;

  @Builder
  public ZooKeeperEntity(Long id,
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

  public String getAddress() {
    return ip + ":" + clientPort;
  }

  public static ZooKeeperEntity of(ZooKeeperDto zookeeperDto) {
    return ModelMapperUtils.map(zookeeperDto, ZooKeeperEntity.class);
  }

  public static List<ZooKeeperEntity> of(List<ZooKeeperDto> zookeeperDtos) {
    return CollectionUtils.emptyIfNull(zookeeperDtos)
        .stream()
        .collect(
            ArrayList::new,
            (zookeeperEntities, zookeeperDto) -> zookeeperEntities.add(of(zookeeperDto)),
            List::addAll);
  }

}
