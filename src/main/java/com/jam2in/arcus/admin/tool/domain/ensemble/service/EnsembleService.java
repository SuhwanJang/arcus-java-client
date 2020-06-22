package com.jam2in.arcus.admin.tool.domain.ensemble.service;

import com.jam2in.arcus.admin.tool.domain.ensemble.component.ZooKeeperFourLetterComponent;
import com.jam2in.arcus.admin.tool.domain.ensemble.component.ZooKeeperZNodeComponent;
import com.jam2in.arcus.admin.tool.domain.ensemble.dto.EnsembleDto;
import com.jam2in.arcus.admin.tool.domain.ensemble.dto.ZooKeeperDto;
import com.jam2in.arcus.admin.tool.domain.ensemble.entity.EnsembleEntity;
import com.jam2in.arcus.admin.tool.domain.ensemble.entity.ZooKeeperEntity;
import com.jam2in.arcus.admin.tool.domain.ensemble.repository.EnsembleRepository;
import com.jam2in.arcus.admin.tool.error.ApiError;
import com.jam2in.arcus.admin.tool.error.ApiErrorCode;
import com.jam2in.arcus.admin.tool.exception.BusinessException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class EnsembleService {

  private final EnsembleRepository ensembleRepository;

  private final ZooKeeperFourLetterComponent fourLetterComponent;

  private final ZooKeeperZNodeComponent znodeComponent;

  public EnsembleService(EnsembleRepository ensembleRepository,
                         ZooKeeperFourLetterComponent fourLetterComponent,
                         ZooKeeperZNodeComponent znodeComponent) {
    this.ensembleRepository = ensembleRepository;
    this.fourLetterComponent = fourLetterComponent;
    this.znodeComponent = znodeComponent;
  }

  @Transactional
  public EnsembleDto create(EnsembleDto ensembleDto) {
    checkDuplicateName(ensembleDto.getName());

    EnsembleEntity ensembleEntity = EnsembleEntity.of(ensembleDto);

    ensembleRepository.save(ensembleEntity);

    return EnsembleDto.ofZooKeepers(ensembleEntity);
  }

  @Transactional
  public EnsembleDto update(long id, EnsembleDto ensembleDto) {
    EnsembleEntity ensembleEntity = getEntity(id);

    if (!ensembleDto.getName().equals(ensembleEntity.getName())) {
      checkDuplicateName(ensembleDto.getName());
      ensembleEntity.updateName(ensembleDto.getName());
    }

    checkDuplicateAddress(ensembleDto, ensembleEntity);
    ensembleEntity.updateZookeepers(ZooKeeperEntity.of(ensembleDto.getZookeepers()));

    ensembleRepository.save(ensembleEntity);

    return EnsembleDto.ofZooKeepers(ensembleEntity);
  }

  public EnsembleDto get(long id) {
    return EnsembleDto.ofZooKeepers(getEntity(id));
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

  public Collection<ZooKeeperDto> getZooKeepers(long id) {
    return EnsembleDto.ofZooKeepers(getEntity(id)).getZookeepers();
  }

  public Collection<ZooKeeperDto> getZooKeeperAllStats(long id) {
    return fourLetterComponent.getAllStats(get(id).getZookeepers());
  }

  public Collection<String> getServiceCodes(long id) {
    return znodeComponent.getServiceCodes(getEntity(id).getZookeepers().stream()
        .map(ZooKeeperEntity::getAddress)
        .collect(Collectors.joining(",")));
  }

  private EnsembleEntity getEntity(long id) {
    return ensembleRepository.findById(id)
        .orElseThrow(() -> new BusinessException(ApiErrorCode.ENSEMBLE_NOT_FOUND));
  }

  private List<EnsembleEntity> getAllEntity() {
    return ensembleRepository.findAll();
  }

  private void checkDuplicateName(String name) {
    if (ensembleRepository.existsByName(name)) {
      throw new BusinessException(ApiErrorCode.ENSEMBLE_NAME_DUPLICATED);
    }
  }

  private void checkDuplicateAddress(EnsembleDto ensembleDto, EnsembleEntity ensembleEntity) {
    // FIXME: use dynamic query or querydsl
    if (CollectionUtils.isEmpty(ensembleDto.getZookeepers())) {
      return;
    }

    Map<String, ZooKeeperEntity> entityMap = ensembleEntity.getZookeepers()
        .stream()
        .collect(
            HashMap::new,
            (map, entity) -> map.put(entity.getAddress(), entity),
            Map::putAll);

    List<String> duplicateAddress = ensembleDto.getZookeepers()
        .stream()
        .filter(dto -> {
          ZooKeeperEntity entity = entityMap.get(dto.getAddress());
          return entity != null && !entity.getId().equals(dto.getId());
        })
        .map(ZooKeeperDto::getAddress)
        .collect(Collectors.toList());

    if (duplicateAddress.isEmpty()) {
      return;
    }

    throw new BusinessException(
        ApiError.of(
          ApiErrorCode.ZOOKEEPER_ADDRESS_DUPLICATED,
          duplicateAddress.stream().map(
              address -> ApiError.Detail.of(StringUtils.EMPTY, address, StringUtils.EMPTY)
          ).collect(Collectors.toList())));
  }

}
