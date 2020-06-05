package com.jam2in.arcus.admin.tool.domain.ensemble.dto;

import com.jam2in.arcus.admin.tool.domain.ensemble.entity.EnsembleEntity;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collection;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;

public class EnsembleDtoTest {

  @Test
  public void builder() {
    // given
    long id = 1L;
    String name = "foo";
    Collection<String> zookeepers = List.of(
        "192.168.0.1:2181",
        "192.168.0.2:2181",
        "192.168.0.3:2181");

    // when
    EnsembleDto ensembleDto = EnsembleDto.builder()
        .id(id)
        .name(name)
        .zookeepers(zookeepers)
        .build();

    // then
    assertThat(ensembleDto.getId(), is(id));
    assertThat(ensembleDto.getName(), is(name));
    assertThat(ensembleDto.getZookeepers(), is(zookeepers));
  }

  @Test
  public void of() {
    // given
    EnsembleEntity ensembleEntity = EnsembleEntity.builder()
        .name("foo")
        .zookeepers(List.of(
            "192.168.0.1:2181",
            "192.168.0.2:2181",
            "192.168.0.3:2181"))
        .build();

    // when
    EnsembleDto ensembleDto = EnsembleDto.of(ensembleEntity);

    // then
    assertThat(ensembleDto.getId(), is(ensembleEntity.getId()));
    assertThat(ensembleDto.getName(), is(ensembleEntity.getName()));
    assertThat(ensembleDto.getZookeepers(), is(ensembleEntity.getZookeepers()));
  }

  @Test
  public void of_list() {
    // given
    EnsembleEntity ensembleEntity1 = EnsembleEntity.builder()
        .name("foo")
        .zookeepers(List.of(
            "192.168.0.1:2181",
            "192.168.0.2:2181",
            "192.168.0.3:2181"
        ))
        .build();
    ReflectionTestUtils.setField(ensembleEntity1, "id", 1L);

    EnsembleEntity ensembleEntity2 = EnsembleEntity.builder()
        .name("bar")
        .zookeepers(List.of(
            "192.168.10.1:2181",
            "192.168.10.2:2181",
            "192.168.10.3:2181"
        ))
        .build();
    ReflectionTestUtils.setField(ensembleEntity1, "id", 2L);

    // when
    List<EnsembleDto> ensembleDtos = EnsembleDto.of(List.of(ensembleEntity1, ensembleEntity2));

    // then
    assertThat(ensembleDtos, contains(
        allOf(
            hasProperty("name", is(ensembleEntity1.getName())),
            hasProperty("zookeepers", is(ensembleEntity1.getZookeepers()))
        ),
        allOf(
            hasProperty("name", is(ensembleEntity2.getName())),
            hasProperty("zookeepers", is(ensembleEntity2.getZookeepers()))
        )
    ));
  }

}
