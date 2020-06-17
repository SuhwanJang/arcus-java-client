package com.jam2in.arcus.admin.tool.domain.ensemble.service;

import com.jam2in.arcus.admin.tool.domain.ensemble.dto.EnsembleDto;
import com.jam2in.arcus.admin.tool.domain.ensemble.dto.ZooKeeperDto;
import com.jam2in.arcus.admin.tool.domain.ensemble.entity.EnsembleEntity;
import com.jam2in.arcus.admin.tool.domain.ensemble.entity.ZooKeeperEntity;
import com.jam2in.arcus.admin.tool.domain.ensemble.repository.EnsembleRepository;
import com.jam2in.arcus.admin.tool.error.ApiErrorCode;
import com.jam2in.arcus.admin.tool.exception.BusinessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
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
    checkDuplicateName(ensembleDto.getName());

    EnsembleEntity ensembleEntity = EnsembleEntity.of(ensembleDto);

    ensembleRepository.save(ensembleEntity);

    return EnsembleDto.of(ensembleEntity);
  }

  @Transactional
  public EnsembleDto update(long id, EnsembleDto ensembleDto) {
    EnsembleEntity ensembleEntity = getEntity(id);

    if (!ensembleDto.getName().equals(ensembleEntity.getName())) {
      checkDuplicateName(ensembleDto.getName());
      ensembleEntity.updateName(ensembleDto.getName());
    }

    return EnsembleDto.of(ensembleEntity);
  }

  @Transactional
  public Collection<ZooKeeperDto> updateZooKeepers(long id,
                                                   Collection<ZooKeeperDto> zookeeperDtos) {
    EnsembleEntity ensembleEntity = getEntity(id);

    ensembleEntity.updateZookeepers(ZooKeeperEntity.of(zookeeperDtos));

    ensembleRepository.save(ensembleEntity);

    return ZooKeeperDto.of(ensembleEntity.getZookeepers());
  }

  public EnsembleDto get(long id) {
    return EnsembleDto.of(getEntity(id));
  }

  public List<EnsembleDto> getAll() {
    return EnsembleDto.of(getAllEntity());
  }

  public Collection<ZooKeeperDto> getZooKeepers(long id) {
    return EnsembleDto.ofZooKeepers(getEntity(id)).getZookeepers();
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

  private void checkDuplicateName(String name) {
    if (ensembleRepository.existsByName(name)) {
      throw new BusinessException(ApiErrorCode.ENSEMBLE_NAME_DUPLICATED);
    }
  }

}
