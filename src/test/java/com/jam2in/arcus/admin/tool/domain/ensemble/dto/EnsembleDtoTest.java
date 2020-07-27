package com.jam2in.arcus.admin.tool.domain.ensemble.dto;

public class EnsembleDtoTest {

  /*
  @Test
  public void builder() {
    // given
    long id = 1L;
    String name = "foo";
    List<String> zookeepers = List.of(
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
    EnsembleDtoUtils.equals(ensembleDto,
        id, name, zookeepers);
  }

  @Test
  public void of() {
    // given
    EnsembleEntity ensembleEntity = EnsembleEntityUtils.createBuilder().build();

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
    EnsembleDtoUtils.equals(ensembleDtos.get(0), ensembleEntity1);
    EnsembleDtoUtils.equals(ensembleDtos.get(1), ensembleEntity2);
  }
   */

}
