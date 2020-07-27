package com.jam2in.arcus.admin.tool.domain.kubernetes.service;

import com.jam2in.arcus.admin.tool.domain.kubernetes.dto.KubernetesDto;
import com.jam2in.arcus.admin.tool.domain.kubernetes.entity.KubernetesEntity;
import com.jam2in.arcus.admin.tool.domain.kubernetes.repository.KubernetesRepository;
import com.jam2in.arcus.admin.tool.error.ApiErrorCode;
import com.jam2in.arcus.admin.tool.exception.BusinessException;
import org.apache.commons.collections4.ListUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class KubernetesService {

  private final KubernetesRepository kubernetesRepository;

  public KubernetesService(KubernetesRepository kubernetesRepository) {
    this.kubernetesRepository = kubernetesRepository;
  }

  @Transactional
  public KubernetesDto create(KubernetesDto kubernetesDto) {
    checkDuplicateName(kubernetesDto.getName());
    checkDuplicateAddress(kubernetesDto.getAddress());

    KubernetesEntity kubernetesEntity = KubernetesEntity.of(kubernetesDto);

    kubernetesRepository.save(kubernetesEntity);

    return KubernetesDto.of(kubernetesEntity);
  }

  @Transactional
  public KubernetesDto update(long id, KubernetesDto kubernetesDto) {
    KubernetesEntity kubernetesEntity = getEntity(id);

    if (!kubernetesDto.getName().equals(kubernetesEntity.getName())) {
      checkDuplicateName(kubernetesDto.getName());
      kubernetesEntity.updateName(kubernetesDto.getName());
    }

    if (!kubernetesDto.getAddress().equals(kubernetesEntity.getName())) {
      checkDuplicateAddress(kubernetesDto.getAddress());
      kubernetesEntity.updateAddress(kubernetesDto.getName());
    }

    kubernetesEntity.updateToken(kubernetesDto.getToken());

    return KubernetesDto.of(kubernetesEntity);
  }

  public KubernetesDto get(long id) {
    return KubernetesDto.of(getEntity(id));
  }

  public List<KubernetesDto> getAll() {
    return KubernetesDto.of(ListUtils.emptyIfNull(kubernetesRepository.findAll()));
  }

  @Transactional
  public void delete(long id) {
    if (!kubernetesRepository.existsById(id)) {
      throw new BusinessException(ApiErrorCode.ENSEMBLE_NOT_FOUND);
    }

    kubernetesRepository.deleteById(id);
  }

  private void checkDuplicateName(String name) {
    if (kubernetesRepository.existsByName(name)) {
      throw new BusinessException(ApiErrorCode.KUBERNETES_NAME_DUPLICATED);
    }
  }

  private void checkDuplicateAddress(String address) {
    if (kubernetesRepository.existsByAddress(address)) {
      throw new BusinessException(ApiErrorCode.KUBERNETES_ADDRESS_DUPLICATED);
    }
  }

  private KubernetesEntity getEntity(long id) {
    return kubernetesRepository.findById(id)
        .orElseThrow(() -> new BusinessException(ApiErrorCode.KUBERNETES_NOT_FOUND));
  }

}
