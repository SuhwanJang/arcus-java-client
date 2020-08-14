package com.jam2in.arcus.admin.tool.domain.ensemble.dto;

import com.jam2in.arcus.admin.tool.domain.common.dto.DateDto;
import com.jam2in.arcus.admin.tool.domain.ensemble.entity.EnsembleEntity;
import com.jam2in.arcus.admin.tool.domain.zookeeper.dto.ZooKeeperDto;
import com.jam2in.arcus.admin.tool.util.ModelMapperUtils;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.modelmapper.PropertyMap;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class EnsembleDto extends DateDto {

  private Long id;

  @Size(min = SIZE_MIN_NAME, max = SIZE_MAX_NAME)
  @NotEmpty
  private String name;

  @Valid
  private List<ZooKeeperDto> zookeepers;

  @Builder
  public EnsembleDto(Long id,
                     String name,
                     List<ZooKeeperDto> zookeepers) {
    this.id = id;
    this.name = name;
    this.zookeepers = zookeepers;
  }

  public static EnsembleDto of(EnsembleEntity ensembleEntity) {
    return ModelMapperUtils.map(ensembleEntity, EnsembleDto.class, TYPE_MAP_NAME);
  }

  public static List<EnsembleDto> of(List<EnsembleEntity> ensembleEntities) {
    return ensembleEntities
        .stream().collect(
            ArrayList::new,
            (ensembleDtos, ensembleEntity) -> ensembleDtos.add(of(ensembleEntity)),
            List::addAll);
  }

  public static EnsembleDto ofZooKeepers(EnsembleEntity ensembleEntity) {
    return ModelMapperUtils.map(ensembleEntity, EnsembleDto.class);
  }

  private static final String TYPE_MAP_NAME = EnsembleDto.class.getSimpleName();

  static {
    ModelMapperUtils.createTypeMap(
        EnsembleEntity.class, EnsembleDto.class,
        TYPE_MAP_NAME).addMappings(
            new PropertyMap<EnsembleEntity, EnsembleDto>() {
              @Override
              protected void configure() {
                skip(destination.zookeepers);
              }
            });
  }

  public static final int SIZE_MIN_NAME = 4;
  public static final int SIZE_MAX_NAME = 32;

}
