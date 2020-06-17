package com.jam2in.arcus.admin.tool.domain.ensemble.dto;

import com.jam2in.arcus.admin.tool.domain.ensemble.entity.ZooKeeperEntity;
import com.jam2in.arcus.admin.tool.util.ModelMapperUtils;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ZooKeeperDto {

  @Builder
  public ZooKeeperDto(Long id, String address) {
    this.id = id;
    this.address = address;
  }

  private Long id;

  @NotEmpty
  private String address;

  private ZooKeeperFourLetterDto stats;

  public void setStats(ZooKeeperFourLetterDto stats) {
    this.stats = stats;
  }

  public static ZooKeeperDto of(ZooKeeperEntity zookeeperEntity) {
    return ModelMapperUtils.map(zookeeperEntity, ZooKeeperDto.class);
  }

  public static Collection<ZooKeeperDto> of(Collection<ZooKeeperEntity> zookeeperEntities) {
    return zookeeperEntities.stream().collect(
        ArrayList::new,
        (zookeeperDtos, zookeeperEntity) -> zookeeperDtos.add(of(zookeeperEntity)),
        List::addAll);
  }

}
