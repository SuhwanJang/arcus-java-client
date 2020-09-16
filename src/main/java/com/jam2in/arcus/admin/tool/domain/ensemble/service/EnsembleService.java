package com.jam2in.arcus.admin.tool.domain.ensemble.service;

import com.jam2in.arcus.admin.tool.domain.agent.service.AdminAgentService;
import com.jam2in.arcus.admin.tool.domain.memcached.component.MemcachedCommandComponent;
import com.jam2in.arcus.admin.tool.domain.memcached.dto.MemcachedClientDto;
import com.jam2in.arcus.admin.tool.domain.memcached.dto.MemcachedClusterDto;
import com.jam2in.arcus.admin.tool.domain.memcached.dto.MemcachedNodeDto;
import com.jam2in.arcus.admin.tool.domain.memcached.dto.MemcachedReplicationClusterDto;
import com.jam2in.arcus.admin.tool.domain.memcached.dto.MemcachedReplicationGroupDto;
import com.jam2in.arcus.admin.tool.domain.ensemble.dto.EnsembleDto;
import com.jam2in.arcus.admin.tool.domain.ensemble.entity.EnsembleEntity;
import com.jam2in.arcus.admin.tool.domain.ensemble.repository.EnsembleRepository;
import com.jam2in.arcus.admin.tool.domain.zookeeper.component.ZooKeeperFourLetterComponent;
import com.jam2in.arcus.admin.tool.domain.zookeeper.component.ZooKeeperZNodeComponent;
import com.jam2in.arcus.admin.tool.domain.zookeeper.dto.ZooKeeperDto;
import com.jam2in.arcus.admin.tool.domain.zookeeper.dto.ZooKeeperParticipantsDto;
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
import java.util.LinkedList;
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

  private final MemcachedCommandComponent commandComponent;

  private final AdminAgentService adminAgentService;

  public EnsembleService(EnsembleRepository ensembleRepository,
                         ZooKeeperFourLetterComponent fourLetterComponent,
                         ZooKeeperZNodeComponent znodeComponent,
                         MemcachedCommandComponent commandComponent,
                         AdminAgentService adminAgentService) {
    this.ensembleRepository = ensembleRepository;
    this.fourLetterComponent = fourLetterComponent;
    this.znodeComponent = znodeComponent;
    this.commandComponent = commandComponent;
    this.adminAgentService = adminAgentService;
  }

  @Transactional
  public EnsembleDto create(EnsembleDto ensembleDto) {
    checkDuplicateName(ensembleDto.getName());
    checkDuplicateZkServers(ensembleDto.getZkservers());

    for (int i = 0; i < CollectionUtils.size(ensembleDto.getZkservers()); i++) {
      ensembleDto.getZkservers().get(i).setMyId(i + 1);
    }

    EnsembleEntity ensembleEntity = EnsembleEntity.of(ensembleDto);

    ensembleRepository.save(ensembleEntity);

    adminAgentService.initializeZooKeeperServer(
        ensembleDto.getZkservers(),
        ZooKeeperParticipantsDto.generateParticipants(ensembleDto.getZkservers()));

    return EnsembleDto.ofZooKeepers(ensembleEntity);
  }

  @Transactional
  public EnsembleDto update(long id, EnsembleDto ensembleDto) {
    EnsembleEntity ensembleEntity = getEntity(id);

    if (!ensembleDto.getName().equals(ensembleEntity.getName())) {
      checkDuplicateName(ensembleDto.getName());
      ensembleEntity.setName(ensembleDto.getName());
    }

    checkDuplicateAddress(ensembleDto);
    ensembleEntity.setZkservers(ZooKeeperEntity.of(ensembleDto.getZkservers()));

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
      throw new BusinessException(ApiError.of(ApiErrorCode.ENSEMBLE_NOT_FOUND));
    }

    ensembleRepository.deleteById(id);
  }

  public List<ZooKeeperDto> getZooKeepers(long id) {
    return ListUtils.emptyIfNull(get(id).getZkservers())
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
            getEntity(id).getAddress()));
  }

  public List<String> getReplicationServiceCodes(long id) {
    return ListUtils.emptyIfNull(
        znodeComponent.getReplicationServiceCodes(
            getEntity(id).getAddress()));
  }

  public void createServiceCode(long id, MemcachedClusterDto clusterDto) {
    znodeComponent.createServiceCode(
        getEntity(id).getAddress(), clusterDto);
  }

  public void createReplicationServiceCode(long id, MemcachedReplicationClusterDto replClusterDto) {
    znodeComponent.createReplicationServiceCode(
        getEntity(id).getAddress(), replClusterDto);
  }

  @SuppressWarnings("unused")
  public void deleteCacheNode(long id, String serviceCode, String cacheNodeAddress) {
    znodeComponent.deleteCacheNode(
        getEntity(id).getAddress(), cacheNodeAddress);
  }

  @SuppressWarnings("unused")
  public void deleteReplicationCacheNode(long id, String serviceCode, String cacheNodeAddress) {
    znodeComponent.deleteReplicationCacheNode(
        getEntity(id).getAddress(), cacheNodeAddress);
  }

  public void deleteServiceCode(long id, String serviceCode) {
    znodeComponent.deleteServiceCode(
        getEntity(id).getAddress(), serviceCode);
  }

  public void deleteReplicationServiceCode(long id, String serviceCode) {
    znodeComponent.deleteReplicationServiceCode(
        getEntity(id).getAddress(), serviceCode);
  }

  public void deleteReplicationGroup(long id, String serviceCode, String group) {
    znodeComponent.deleteReplicationGroup(
        getEntity(id).getAddress(), serviceCode, group);
  }

  public List<MemcachedNodeDto> getCacheNodes(long id, String serviceCode) {
    return ListUtils.emptyIfNull(
        znodeComponent.getCacheNodes(
            getEntity(id).getAddress(), serviceCode))
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

  public List<MemcachedReplicationGroupDto> getReplicationCacheNodes(long id,
                                                                     String serviceCode) {
    return ListUtils.emptyIfNull(
        znodeComponent.getReplicationCacheNodes(
            getEntity(id).getAddress(), serviceCode))
        .stream()
        .map(group -> {
          CompletableFuture<MemcachedReplicationGroupDto> future = null;

          if (group.getNode1() != null) {
            future = commandComponent.stats(group.getNode1().getNodeAddress())
                .thenApply(stats -> {
                  group.getNode1().setStats(stats);
                  return group;
                });

            if (group.getNode2() != null) {
              future = future.thenCombine(
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

  public List<MemcachedClientDto> getCacheClients(long id, String serviceCode) {
    return ListUtils.emptyIfNull(
        znodeComponent.getCacheClients(
            getEntity(id).getAddress(), serviceCode));
  }

  public List<MemcachedClientDto> getReplicationCacheClients(long id, String serviceCode) {
    return ListUtils.emptyIfNull(
        znodeComponent.getReplicationCacheClients(
            getEntity(id).getAddress(), serviceCode));
  }

  private EnsembleEntity getEntity(long id) {
    return ensembleRepository.findById(id)
        .orElseThrow(() -> new BusinessException(ApiError.of(ApiErrorCode.ENSEMBLE_NOT_FOUND)));
  }

  private void checkDuplicateName(String name) {
    if (ensembleRepository.existsByName(name)) {
      throw new BusinessException(ApiError.of(ApiErrorCode.ENSEMBLE_DUPLICATED_NAME));
    }
  }

  private void checkDuplicateZkServers(List<ZooKeeperDto> zkservers) {
    Set<String> clientPorts = new HashSet<>();
    Set<String> serverPorts = new HashSet<>();
    Set<String> electionPorts = new HashSet<>();

    List<ApiError.Detail> details = new LinkedList<>();

    for (int i = 0; i < CollectionUtils.size(zkservers); i++) {
      ZooKeeperDto zookeeperDto = zkservers.get(i);

      if (!clientPorts.add(zookeeperDto.getIp() + ":" + zookeeperDto.getClientPort())) {
        details.add(
            ApiError.Detail.of(
                String.format("zkservers[%d].clientPorts", i),
                String.valueOf(zookeeperDto.getClientPort())));
      }

      if (!serverPorts.add(zookeeperDto.getIp() + ":" + zookeeperDto.getServerPort())) {
        details.add(
            ApiError.Detail.of(
                String.format("zkservers[%d].serverPorts", i),
                String.valueOf(zookeeperDto.getServerPort())));
      }

      if (!electionPorts.add(zookeeperDto.getIp() + ":" + zookeeperDto.getElectionPort())) {
        details.add(
            ApiError.Detail.of(
                String.format("zkservers[%d].electionPorts", i),
                String.valueOf(zookeeperDto.getElectionPort())));
      }
    }

    if (details.size() > 0) {
      throw new BusinessException(ApiError.of(ApiErrorCode.ENSEMBLE_DUPLICATED_PORT, details));
    }
  }

  private void checkDuplicateAddress(EnsembleDto ensembleDto) {
    if (CollectionUtils.isEmpty(ensembleDto.getZkservers())) {
      return;
    }

    Set<String> addresses = new HashSet<>();

    Set<String> duplicateAddress = ensembleDto.getZkservers()
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
