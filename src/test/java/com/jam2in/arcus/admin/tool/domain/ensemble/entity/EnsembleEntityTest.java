package com.jam2in.arcus.admin.tool.domain.ensemble.entity;

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

}
