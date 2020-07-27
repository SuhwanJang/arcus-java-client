package com.jam2in.arcus.admin.tool.domain.ensemble.service;

import com.jam2in.arcus.admin.tool.domain.cache.component.CacheCommandComponent;
import com.jam2in.arcus.admin.tool.domain.cache.dto.CacheClientsDto;
import com.jam2in.arcus.admin.tool.domain.cache.dto.CacheClusterDto;
import com.jam2in.arcus.admin.tool.domain.cache.dto.CacheNodeDto;
import com.jam2in.arcus.admin.tool.domain.cache.dto.ReplicationCacheClusterDto;
import com.jam2in.arcus.admin.tool.domain.cache.dto.ReplicationCacheGroupDto;
import com.jam2in.arcus.admin.tool.domain.ensemble.dto.EnsembleDto;
import com.jam2in.arcus.admin.tool.domain.ensemble.entity.EnsembleEntity;
import com.jam2in.arcus.admin.tool.domain.ensemble.repository.EnsembleRepository;
import com.jam2in.arcus.admin.tool.domain.zookeeper.component.ZooKeeperFourLetterComponent;
import com.jam2in.arcus.admin.tool.domain.zookeeper.component.ZooKeeperZNodeComponent;
import com.jam2in.arcus.admin.tool.domain.zookeeper.dto.ZooKeeperDto;
import com.jam2in.arcus.admin.tool.domain.zookeeper.entity.ZooKeeperEntity;
import com.jam2in.arcus.admin.tool.error.ApiError;
import com.jam2in.arcus.admin.tool.error.ApiErrorCode;
import com.jam2in.arcus.admin.tool.exception.BusinessException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class EnsembleService {

  private final EnsembleRepository ensembleRepository;

  private final ZooKeeperFourLetterComponent fourLetterComponent;

  private final ZooKeeperZNodeComponent znodeComponent;

  private final CacheCommandComponent commandComponent;

  public EnsembleService(EnsembleRepository ensembleRepository,
                         ZooKeeperFourLetterComponent fourLetterComponent,
                         ZooKeeperZNodeComponent znodeComponent,
                         CacheCommandComponent commandComponent) {
    this.ensembleRepository = ensembleRepository;
    this.fourLetterComponent = fourLetterComponent;
    this.znodeComponent = znodeComponent;
    this.commandComponent = commandComponent;
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

    checkDuplicateAddress(ensembleDto);
    ensembleEntity.updateZookeepers(ZooKeeperEntity.of(ensembleDto.getZookeepers()));

    ensembleRepository.save(ensembleEntity);

    return EnsembleDto.ofZooKeepers(ensembleEntity);
  }

  public EnsembleDto get(long id) {
    return EnsembleDto.ofZooKeepers(getEntity(id));
  }

  public List<EnsembleDto> getAll() {
    return EnsembleDto.of(ListUtils.emptyIfNull(ensembleRepository.findAll()));
  }

  @Transactional
  public void delete(long id) {
    if (!ensembleRepository.existsById(id)) {
      throw new BusinessException(ApiErrorCode.ENSEMBLE_NOT_FOUND);
    }

    ensembleRepository.deleteById(id);
  }

  public List<ZooKeeperDto> getZooKeepers(long id) {
    return ListUtils.emptyIfNull(get(id).getZookeepers())
        .stream()
        .map(zookeeperDto -> fourLetterComponent.getStats(zookeeperDto.getAddress())
            .thenApply(stats -> {
              zookeeperDto.setStats(stats);
              return zookeeperDto;
            }))
        .collect(Collectors.toList())
        .stream()
        .map(CompletableFuture::join)
        .collect(Collectors.toList());
  }

  public List<String> getServiceCodes(long id) {
    return ListUtils.emptyIfNull(
        znodeComponent.getServiceCodes(
            EnsembleEntity.joiningZooKeeperAddresses(getEntity(id))));
  }

  public List<String> getReplicationServiceCodes(long id) {
    return ListUtils.emptyIfNull(
        znodeComponent.getReplicationServiceCodes(
            EnsembleEntity.joiningZooKeeperAddresses(getEntity(id))));
  }

  public void createServiceCode(long id, CacheClusterDto clusterDto) {
    znodeComponent.createServiceCode(
        EnsembleEntity.joiningZooKeeperAddresses(getEntity(id)), clusterDto);
  }

  public void createReplicationServiceCode(long id, ReplicationCacheClusterDto replClusterDto) {
    znodeComponent.createReplicationServiceCode(
        EnsembleEntity.joiningZooKeeperAddresses(getEntity(id)), replClusterDto);
  }

  @SuppressWarnings("unused")
  public void deleteCacheNode(long id, String serviceCode, String cacheNodeAddress) {
    znodeComponent.deleteCacheNode(
        EnsembleEntity.joiningZooKeeperAddresses(getEntity(id)), cacheNodeAddress);
  }

  @SuppressWarnings("unused")
  public void deleteReplicationCacheNode(long id, String serviceCode, String cacheNodeAddress) {
    znodeComponent.deleteReplicationCacheNode(
        EnsembleEntity.joiningZooKeeperAddresses(getEntity(id)), cacheNodeAddress);
  }

  public void deleteServiceCode(long id, String serviceCode) {
    znodeComponent.deleteServiceCode(
        EnsembleEntity.joiningZooKeeperAddresses(getEntity(id)), serviceCode);
  }

  public void deleteReplicationServiceCode(long id, String serviceCode) {
    znodeComponent.deleteReplicationServiceCode(
        EnsembleEntity.joiningZooKeeperAddresses(getEntity(id)), serviceCode);
  }

  public void deleteReplicationGroup(long id, String serviceCode, String group) {
    znodeComponent.deleteReplicationGroup(
        EnsembleEntity.joiningZooKeeperAddresses(getEntity(id)), serviceCode, group);
  }

  public List<CacheNodeDto> getCacheNodes(long id, String serviceCode) {
    return ListUtils.emptyIfNull(
        znodeComponent.getCacheNodes(
            EnsembleEntity.joiningZooKeeperAddresses(
                getEntity(id)), serviceCode))
        .stream()
        .map(cacheNodeDto -> commandComponent.stats(cacheNodeDto.getAddress())
            .thenApply(stats -> {
              cacheNodeDto.setStats(stats);
              return cacheNodeDto;
            }))
        .collect(Collectors.toList())
        .stream()
        .map(CompletableFuture::join)
        .collect(Collectors.toList());
  }

  public List<ReplicationCacheGroupDto> getReplicationCacheNodes(long id,
                                                                 String serviceCode) {
    return ListUtils.emptyIfNull(
        znodeComponent.getReplicationCacheNodes(
            EnsembleEntity.joiningZooKeeperAddresses(getEntity(id)), serviceCode))
        .stream()
        .map(group -> {
          CompletableFuture<ReplicationCacheGroupDto> future = null;
          if (group.getNode1() != null) {
            future = commandComponent.stats(group.getNode1().getNodeAddress())
                .thenApply(stats -> {
                  group.getNode1().setStats(stats);
                  return group;
                });
            if (group.getNode2() != null) {
              future.thenCombine(
                  commandComponent.stats(group.getNode2().getNodeAddress()),
                  (grp, node2Stats) -> {
                    group.getNode2().setStats(node2Stats);
                    return group;
                  });
            }
          }
          return future == null ? CompletableFuture.completedFuture(group) : future;
        })
        .collect(Collectors.toList())
        .stream()
        .map(CompletableFuture::join)
        .collect(Collectors.toList());
  }

  public List<CacheClientsDto> getCacheClients(long id, String serviceCode) {
    return ListUtils.emptyIfNull(
        znodeComponent.getCacheClients(
            EnsembleEntity.joiningZooKeeperAddresses(getEntity(id)), serviceCode));
  }

  public List<CacheClientsDto> getReplicationCacheClients(long id, String serviceCode) {
    return ListUtils.emptyIfNull(
        znodeComponent.getReplicationCacheClients(
            EnsembleEntity.joiningZooKeeperAddresses(getEntity(id)), serviceCode));
  }

  private EnsembleEntity getEntity(long id) {
    return ensembleRepository.findById(id)
        .orElseThrow(() -> new BusinessException(ApiErrorCode.ENSEMBLE_NOT_FOUND));
  }

  private void checkDuplicateName(String name) {
    if (ensembleRepository.existsByName(name)) {
      throw new BusinessException(ApiErrorCode.ENSEMBLE_NAME_DUPLICATED);
    }
  }

  private void checkDuplicateAddress(EnsembleDto ensembleDto) {
    if (CollectionUtils.isEmpty(ensembleDto.getZookeepers())) {
      return;
    }

    Set<String> addresses = new HashSet<>();

    Set<String> duplicateAddress = ensembleDto.getZookeepers()
        .stream()
        .map(ZooKeeperDto::getAddress)
        .filter(address -> {
          if (addresses.contains(address)) {
            return true;
          }
          addresses.add(address);
          return false;
        })
        .collect(Collectors.toSet());

    if (duplicateAddress.isEmpty()) {
      return;
    }

    throw new BusinessException(
        ApiError.of(
          ApiErrorCode.ZOOKEEPER_ADDRESS_DUPLICATED,
          duplicateAddress
              .stream()
              .map(address -> ApiError.Detail.of(StringUtils.EMPTY, address, StringUtils.EMPTY))
              .collect(Collectors.toList())));
  }

}
