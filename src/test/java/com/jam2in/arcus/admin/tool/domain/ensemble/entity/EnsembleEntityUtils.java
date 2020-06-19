package com.jam2in.arcus.admin.tool.domain.ensemble.entity;

import com.jam2in.arcus.admin.tool.domain.ensemble.dto.EnsembleDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EnsembleEntityUtils {

  /*
  public static EnsembleEntity.EnsembleEntityBuilder createBuilder() {
    EnsembleDto ensembleDto = EnsembleDto.builder().build();

    return EnsembleEntity.builder()
        .name(ensembleDto.getName() + "n")
        .zookeepers(List.of(
            "192.168.10.1:2181",
            "192.168.10.2:2181",
            "192.168.10.3:2181"));
  }

  public static void equals(EnsembleDto ensembleDto,
                            Long id,
                            String name,
                            Collection<String> zookeepers) {
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
