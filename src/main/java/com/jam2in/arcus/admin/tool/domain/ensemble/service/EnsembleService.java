package com.jam2in.arcus.admin.tool.domain.ensemble.service;

import com.jam2in.arcus.admin.tool.domain.ensemble.dto.EnsembleDto;
import com.jam2in.arcus.admin.tool.domain.ensemble.entity.EnsembleEntity;
import com.jam2in.arcus.admin.tool.domain.ensemble.repository.EnsembleRepository;
import com.jam2in.arcus.admin.tool.exception.ApiErrorCode;
import com.jam2in.arcus.admin.tool.exception.BusinessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class EnsembleService {

  private final EnsembleRepository ensembleRepository;

  public EnsembleService(EnsembleRepository ensembleRepository) {
    this.ensembleRepository = ensembleRepository;
  }

  @Transactional
  public EnsembleDto create(EnsembleDto ensembleDto) {
    EnsembleEntity ensembleEntity = EnsembleEntity.of(ensembleDto);

    ensembleRepository.save(ensembleEntity);

    return EnsembleDto.of(ensembleEntity);
  }

  @Transactional
  public EnsembleDto update(long id, EnsembleDto ensembleDto) {
    EnsembleEntity ensembleEntity = getEntity(id);

    ensembleEntity.updateName(ensembleDto.getName());
    ensembleEntity.updateZookeepers(ensembleDto.getZookeepers());

    return EnsembleDto.of(ensembleEntity);
  }

  public EnsembleDto get(long id) {
    return EnsembleDto.of(getEntity(id));
  }

  public List<EnsembleDto> getAll() {
    return EnsembleDto.of(getAllEntity());
  }

  @Transactional
  public void delete(long id) {
    if (!ensembleRepository.existsById(id)) {
      throw new BusinessException(ApiErrorCode.ENSEMBLE_NOT_FOUND);
    }

    ensembleRepository.deleteById(id);
  }

  private EnsembleEntity getEntity(long id) {
    return ensembleRepository.findById(id)
        .orElseThrow(() -> new BusinessException(ApiErrorCode.ENSEMBLE_NOT_FOUND));
  }

  private List<EnsembleEntity> getAllEntity() {
    List<EnsembleEntity> ensembles = ensembleRepository.findAll();

    if (ensembles.isEmpty()) {
      throw new BusinessException(ApiErrorCode.ENSEMBLE_NO_ENSEMBLES);
    }

    return ensembles;
  }

}
