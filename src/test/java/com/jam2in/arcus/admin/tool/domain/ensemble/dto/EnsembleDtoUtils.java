package com.jam2in.arcus.admin.tool.domain.ensemble.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EnsembleDtoUtils {

  /*
  public static EnsembleDto.EnsembleDtoBuilder createBuilder() {
    return EnsembleDto.builder()
        .id(1L)
        .name(StringUtils.repeat('u', EnsembleDto.SIZE_MIN_NAME))
        .zookeepers(List.of(
            "192.168.0.1:2181",
            "192.168.0.2:2181",
            "192.168.0.3:2181"));
  }

  public static void equals(EnsembleDto ensembleDto,
                            Long id,
                            String name,
                            List<String> zookeepers) {
    assertThat(ensembleDto.getId(), is(id));
    assertThat(ensembleDto.getName(), is(name));
    assertThat(ensembleDto.getZookeepers(), is(zookeepers));
  }

  public static void equals(EnsembleDto ensembleDto, EnsembleEntity ensembleEntity) {
    assertThat(ensembleDto.getId(), is(ensembleEntity.getId()));
    assertThat(ensembleDto.getName(), is(ensembleEntity.getName()));
    assertThat(ensembleDto.getZookeepers(), is(ensembleEntity.getZookeepers()));
  }
   */

}
