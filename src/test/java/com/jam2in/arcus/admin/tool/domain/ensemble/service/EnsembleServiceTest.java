package com.jam2in.arcus.admin.tool.domain.ensemble.service;

import com.jam2in.arcus.admin.tool.domain.ensemble.dto.EnsembleDto;
import com.jam2in.arcus.admin.tool.domain.ensemble.dto.EnsembleDtoUtils;
import com.jam2in.arcus.admin.tool.domain.ensemble.entity.EnsembleEntity;
import com.jam2in.arcus.admin.tool.domain.ensemble.entity.EnsembleEntityUtils;
import com.jam2in.arcus.admin.tool.domain.ensemble.repository.EnsembleRepository;
import com.jam2in.arcus.admin.tool.exception.ApiErrorCode;
import com.jam2in.arcus.admin.tool.exception.BusinessException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
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

  private EnsembleDto ensembleDto;

  private EnsembleEntity ensembleEntity;

  @Before
  public void before() {
    ensembleDto = EnsembleDtoUtils.createBuilder().build();
    ensembleEntity = EnsembleEntityUtils.createBuilder().build();
  }

  @Test
  public void create() {
    // given
    // when
    ensembleService.create(ensembleDto);

    // then
    verify(ensembleRepository, atMostOnce()).save(any());
  }

  @Test
  public void create_duplicateName() {
    // given
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
    given(ensembleRepository.findById(ensembleDto.getId()))
        .willReturn(Optional.empty());

    try {
      // when
      ensembleService.update(ensembleDto.getId(), ensembleDto);
    } catch (BusinessException e) {
      // then
      assertThat(e.getApiError().getCode(), is(ApiErrorCode.ENSEMBLE_NOT_FOUND.code()));
      return;
    }

    fail();
  }

  @Test
  public void update_duplicateName() {
    // given
    given(ensembleRepository.findById(ensembleDto.getId()))
        .willReturn(Optional.of(ensembleEntity));
    given(ensembleRepository.existsByName(ensembleDto.getName()))
        .willReturn(true);

    try {
      // when
      ensembleService.update(ensembleDto.getId(), ensembleDto);
    } catch (BusinessException e) {
      assertThat(e.getApiError().getCode(), is(ApiErrorCode.ENSEMBLE_NAME_DUPLICATED.code()));
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
