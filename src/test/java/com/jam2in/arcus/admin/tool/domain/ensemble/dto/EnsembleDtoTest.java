package com.jam2in.arcus.admin.tool.domain.ensemble.dto;

import org.junit.Test;

import java.util.Collection;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
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

}
