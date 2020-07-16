package com.jam2in.arcus.admin.tool.domain.zookeeper.entity;

import com.jam2in.arcus.admin.tool.domain.base.entity.BaseEntity;
import com.jam2in.arcus.admin.tool.domain.zookeeper.dto.ZooKeeperDto;
import com.jam2in.arcus.admin.tool.util.ModelMapperUtils;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(
    name = "zookeepers",
    uniqueConstraints = @UniqueConstraint(
        columnNames = {
            "ensemble_id", "address"
        })
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ZooKeeperEntity extends BaseEntity {

  @Builder
  public ZooKeeperEntity(Long id, String address) {
    this.id = id;
    this.address = address;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String address;

  public static ZooKeeperEntity of(ZooKeeperDto zookeeperDto) {
    return ModelMapperUtils.map(zookeeperDto, ZooKeeperEntity.class);
  }

  public static Collection<ZooKeeperEntity> of(Collection<ZooKeeperDto> zookeeperDtos) {
    if (zookeeperDtos == null) {
      return null;
    }

    return zookeeperDtos
        .stream()
        .collect(
            ArrayList::new,
            (zookeperEntities, zookeeperDto) -> zookeperEntities.add(of(zookeeperDto)),
            List::addAll);
  }

}