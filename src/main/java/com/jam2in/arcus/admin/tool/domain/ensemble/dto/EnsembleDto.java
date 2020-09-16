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

  @Size(min = NAME_SIZE_MIN, max = NAME_SIZE_MAX)
  @NotEmpty
  private String name;

  private String address;

  @Valid
  private List<ZooKeeperDto> zkservers;

  @Builder
  public EnsembleDto(Long id,
                     String name,
                     String address,
                     List<ZooKeeperDto> zkservers) {
    this.id = id;
    this.name = name;
    this.address = address;
    this.zkservers = zkservers;
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
                skip(destination.zkservers);
              }
            });
  }

  public static final int NAME_SIZE_MIN = 4;
  public static final int NAME_SIZE_MAX = 32;

}
