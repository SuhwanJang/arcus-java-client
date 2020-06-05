package com.jam2in.arcus.admin.tool.domain.ensemble.entity;

import com.jam2in.arcus.admin.tool.domain.ensemble.dto.EnsembleDto;
import org.junit.Test;

import java.util.Collection;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class EnsembleEntityTest {

  @Test
  public void builder() {
    // given
    String name = "foo";
    Collection<String> zookeepers = List.of(
        "192.168.0.1:2181",
        "192.168.0.2:2181",
        "192.168.0.3:2181");

    // when
    EnsembleEntity ensembleEntity = EnsembleEntity.builder()
        .name(name)
        .zookeepers(zookeepers)
        .build();

    // then
    assertThat(ensembleEntity.getName(), is(name));
    assertThat(ensembleEntity.getZookeepers(), is(zookeepers));
  }

  @Test
  public void of() {
    // given
    EnsembleDto ensembleDto = EnsembleDto.builder()
        .name("foo")
        .zookeepers(List.of(
            "192.168.0.1:2181",
            "192.168.0.2:2181",
            "192.168.0.3:2181"))
        .build();

    // when
    EnsembleEntity ensembleEntity = EnsembleEntity.of(ensembleDto);

    // then
    assertThat(ensembleEntity.getName(), is(ensembleDto.getName()));
    assertThat(ensembleEntity.getZookeepers(), is(ensembleDto.getZookeepers()));
  }

  @Test
  public void updateName() {
    // given
    EnsembleEntity ensembleEntity = EnsembleEntity.builder().name("foo").build();

    // when
    ensembleEntity.updateName("bar");

    // then
    assertThat(ensembleEntity.getName(), is("bar"));
  }

  @Test
  public void updateZookeepers() {
    // given
    EnsembleEntity ensembleEntity = EnsembleEntity.builder()
        .zookeepers(List.of(
            "192.168.0.1:2181",
            "192.168.0.2:2181",
            "192.168.0.3:2181"))
        .build();

    // when
    ensembleEntity.updateZookeepers(List.of(
        "192.168.10.1:2181",
        "192.168.10.2:2181",
        "192.168.10.3:2181"
    ));

    // then
    assertThat(ensembleEntity.getZookeepers(), is(List.of(
        "192.168.10.1:2181",
        "192.168.10.2:2181",
        "192.168.10.3:2181"
    )));
  }

}
