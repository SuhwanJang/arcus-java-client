package com.jam2in.arcus.admin.tool.domain.ensemble.dto;

import com.jam2in.arcus.admin.tool.domain.ensemble.entity.EnsembleEntity;
import com.jam2in.arcus.admin.tool.util.ModelMapperUtil;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class EnsembleDto {

  @Builder
  public EnsembleDto(Long id, String name, Collection<String> zookeepers) {
    this.id = id;
    this.name = name;
    this.zookeepers = zookeepers;
  }

  private Long id;

  @Size(min = SIZE_MIN_NAME, max = SIZE_MAX_NAME)
  @NotEmpty
  private String name;

  private Collection<@NotNull String> zookeepers;

  public static EnsembleDto of(EnsembleEntity ensembleEntity) {
    return ModelMapperUtil.map(ensembleEntity, EnsembleDto.class);
  }

  public static List<EnsembleDto> of(List<EnsembleEntity> ensembleEntities) {
    return ensembleEntities.stream().collect(
        ArrayList::new,
        (ensembleDtos, ensembleEntity) -> ensembleDtos.add(of(ensembleEntity)),
        List::addAll);
  }

  public static final int SIZE_MIN_NAME = 4;
  public static final int SIZE_MAX_NAME = 32;

}
