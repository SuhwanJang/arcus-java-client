package com.jam2in.arcus.admin.tool.domain.ensemble.service;

import com.jam2in.arcus.admin.tool.domain.ensemble.dto.EnsembleDto;
import com.jam2in.arcus.admin.tool.domain.ensemble.entity.EnsembleEntity;
import com.jam2in.arcus.admin.tool.domain.ensemble.repository.EnsembleRepository;
import com.jam2in.arcus.admin.tool.exception.ApiErrorCode;
import com.jam2in.arcus.admin.tool.exception.BusinessException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class EnsembleServiceTest {

  @InjectMocks
  private EnsembleService ensembleService;

  @Mock
  private EnsembleRepository ensembleRepository;

  @Test
  public void create() {
    // given
    EnsembleDto ensembleDto = EnsembleDto.builder()
        .name("foo")
        .zookeepers(List.of(
            "192.168.0.1:2181",
            "192.168.0.2:2181",
            "192.168.0.3:2181"))
        .build();

    // when
    ensembleService.create(ensembleDto);

    // then
    verify(ensembleRepository, atMostOnce()).save(any());
  }

  @Test
  public void create_duplicateName() {
    // given
    EnsembleDto ensembleDto = EnsembleDto.builder().name("foo").build();

    given(ensembleRepository.existsByName(ensembleDto.getName())).willReturn(true);

    try {
      // when
      ensembleService.create(ensembleDto);
    } catch (BusinessException e) {
      // then
      verify(ensembleRepository, never()).save(any());
      assertThat(e.getApiError().getCode(), is(ApiErrorCode.ENSEMBLE_NAME_DUPLICATED.code()));
      return;
    }

    fail();
  }

  @Test
  public void update() {
    // given
    EnsembleDto ensembleDto = EnsembleDto.builder()
        .id(1L)
        .name("foo")
        .zookeepers(List.of(
            "192.168.0.1:2181",
            "192.168.0.2:2181",
            "192.168.0.3:2181"
        ))
        .build();

    EnsembleEntity ensembleEntity = EnsembleEntity.builder()
        .name(ensembleDto.getName() + " n")
        .zookeepers(List.of(
            "192.168.10.1:2181",
            "192.168.10.2:2181",
            "192.168.10.3:2181"
        ))
        .build();

    given(ensembleRepository.findById(ensembleDto.getId()))
        .willReturn(Optional.of(ensembleEntity));

    // when
    ensembleService.update(ensembleDto.getId(), ensembleDto);

    // then
    assertThat(ensembleEntity.getName(), is(ensembleDto.getName()));
    assertThat(ensembleEntity.getZookeepers(), is(ensembleDto.getZookeepers()));
  }

  @Test
  public void update_notFound() {
    // given
    EnsembleDto ensembleDto = EnsembleDto.builder()
        .id(1L)
        .name("foo")
        .zookeepers(List.of(
            "192.168.0.1:2181",
            "192.168.0.2:2181",
            "192.168.0.3:2181"
        ))
        .build();

    EnsembleEntity ensembleEntity = EnsembleEntity.builder()
        .name(ensembleDto.getName() + " n")
        .zookeepers(List.of(
            "192.168.10.1:2181",
            "192.168.10.2:2181",
            "192.168.10.3:2181"
        ))
        .build();

    given(ensembleRepository.findById(ensembleDto.getId()))
        .willReturn(Optional.empty());

    try {
      // when
      ensembleService.update(ensembleDto.getId(), ensembleDto);
    } catch (BusinessException e) {
      // then
      assertThat(e.getApiError().getCode(), is(ApiErrorCode.ENSEMBLE_NOT_FOUND.code()));
      assertThat(ensembleEntity.getName(), is(not(ensembleDto.getName())));
      assertThat(ensembleEntity.getZookeepers(), is(not(ensembleDto.getZookeepers())));
      return;
    }

    fail();
  }

  @Test
  public void delete() {
    // given
    long id = 1L;

    given(ensembleRepository.existsById(id)).willReturn(true);

    // when
    ensembleService.delete(id);

    // then
    verify(ensembleRepository, atMostOnce()).deleteById(id);
  }

  @Test
  public void delete_notFound() {
    // given
    long id = 1L;

    given(ensembleRepository.existsById(id)).willReturn(false);

    try {
      // when
      ensembleService.delete(id);
    } catch (BusinessException e) {
      assertThat(e.getApiError().getCode(), is(ApiErrorCode.ENSEMBLE_NOT_FOUND.code()));
      return;
    }

    fail();
  }

}
