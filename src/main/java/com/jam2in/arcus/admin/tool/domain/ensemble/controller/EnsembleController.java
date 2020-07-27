package com.jam2in.arcus.admin.tool.domain.ensemble.controller;

import com.jam2in.arcus.admin.tool.domain.cache.dto.CacheClientsDto;
import com.jam2in.arcus.admin.tool.domain.cache.dto.CacheClusterDto;
import com.jam2in.arcus.admin.tool.domain.cache.dto.CacheNodeDto;
import com.jam2in.arcus.admin.tool.domain.cache.dto.ReplicationCacheClusterDto;
import com.jam2in.arcus.admin.tool.domain.cache.dto.ReplicationCacheGroupDto;
import com.jam2in.arcus.admin.tool.domain.ensemble.dto.EnsembleDto;
import com.jam2in.arcus.admin.tool.domain.ensemble.service.EnsembleService;
import com.jam2in.arcus.admin.tool.domain.zookeeper.dto.ZooKeeperDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/ensembles")
public class EnsembleController {

  private final EnsembleService ensembleService;

  public EnsembleController(EnsembleService ensembleService) {
    this.ensembleService = ensembleService;
  }

  @PostMapping
  @ResponseStatus(code = HttpStatus.CREATED)
  public EnsembleDto create(@RequestBody @Valid EnsembleDto ensembleDto) {
    return ensembleService.create(ensembleDto);
  }

  @PutMapping("/{id}")
  @ResponseStatus(code = HttpStatus.OK)
  public EnsembleDto update(@PathVariable long id,
                            @RequestBody @Valid EnsembleDto ensembleDto) {
    return ensembleService.update(id, ensembleDto);
  }

  @GetMapping("/{id}")
  @ResponseStatus(code = HttpStatus.OK)
  public EnsembleDto get(@PathVariable long id) {
    return ensembleService.get(id);
  }

  @GetMapping
  @ResponseStatus(code = HttpStatus.OK)
  public List<EnsembleDto> getAll() {
    return ensembleService.getAll();
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(code = HttpStatus.OK)
  public void delete(@PathVariable long id) {
    ensembleService.delete(id);
  }

  @GetMapping("/{id}/zookeepers")
  @ResponseStatus(code = HttpStatus.OK)
  public List<ZooKeeperDto> getZooKeepers(@PathVariable long id) {
    return ensembleService.getZooKeepers(id);
  }

  @PostMapping("/{id}/service-codes")
  @ResponseStatus(code = HttpStatus.CREATED)
  public void createServiceCode(@PathVariable long id,
                                @RequestBody @Valid CacheClusterDto clusterDto) {
    ensembleService.createServiceCode(id, clusterDto);
  }

  @PostMapping("/{id}/repl-service-codes")
  @ResponseStatus(code = HttpStatus.CREATED)
  public void createReplicationServiceCode(
      @PathVariable long id,
      @RequestBody @Valid ReplicationCacheClusterDto replClusterDto) {
    ensembleService.createReplicationServiceCode(id, replClusterDto);
  }

  @GetMapping("/{id}/service-codes")
  @ResponseStatus(code = HttpStatus.OK)
  public List<String> getServiceCodes(@PathVariable long id) {
    return ensembleService.getServiceCodes(id);
  }

  @GetMapping("/{id}/repl-service-codes")
  @ResponseStatus(code = HttpStatus.OK)
  public List<String> getReplicationServiceCodes(@PathVariable long id) {
    return ensembleService.getReplicationServiceCodes(id);
  }

  @DeleteMapping("/{id}/service-codes/{service-code}")
  @ResponseStatus(code = HttpStatus.OK)
  public void deleteServiceCode(
      @PathVariable("id") long id,
      @PathVariable("service-code") String serviceCode) {
    ensembleService.deleteServiceCode(id, serviceCode);
  }

  @DeleteMapping("/{id}/repl-service-codes/{service-code}")
  @ResponseStatus(code = HttpStatus.OK)
  public void deleteReplicationServiceCode(
      @PathVariable("id") long id,
      @PathVariable("service-code") String serviceCode) {
    ensembleService.deleteReplicationServiceCode(id, serviceCode);
  }

  @DeleteMapping("/{id}/service-codes/{service-code}/cache-nodes/{cache-node-address}")
  @ResponseStatus(code = HttpStatus.OK)
  public void deleteCacheNode(
      @PathVariable("id") long id,
      @PathVariable("service-code") String serviceCode,
      @PathVariable("cache-node-address") String cacheNodeAddress) {
    ensembleService.deleteCacheNode(id, serviceCode, cacheNodeAddress);
  }

  @DeleteMapping("/{id}/repl-service-codes/{service-code}/cache-nodes/{cache-node-address}")
  @ResponseStatus(code = HttpStatus.OK)
  public void deleteReplicationCacheNode(
      @PathVariable("id") long id,
      @PathVariable("service-code") String serviceCode,
      @PathVariable("cache-node-address") String cacheNodeAddress) {
    ensembleService.deleteReplicationCacheNode(id, serviceCode, cacheNodeAddress);
  }

  @DeleteMapping("/{id}/repl-service-codes/{service-code}/groups/{group}")
  @ResponseStatus(code = HttpStatus.OK)
  public void deleteReplicationGroup(@PathVariable("id") long id,
                                     @PathVariable("service-code") String serviceCode,
                                     @PathVariable("group") String group) {
    ensembleService.deleteReplicationGroup(id, serviceCode, group);
  }

  @GetMapping("/{id}/service-codes/{service-code}/cache-nodes")
  @ResponseStatus(code = HttpStatus.OK)
  public List<CacheNodeDto> getCacheNodes(
      @PathVariable("id") long id,
      @PathVariable("service-code") String serviceCode) {
    return ensembleService.getCacheNodes(id, serviceCode);
  }

  @GetMapping("/{id}/repl-service-codes/{service-code}/cache-nodes")
  @ResponseStatus(code = HttpStatus.OK)
  public List<ReplicationCacheGroupDto> getReplicationCacheNodes(
      @PathVariable("id") long id,
      @PathVariable("service-code") String serviceCode) {
    return ensembleService.getReplicationCacheNodes(id, serviceCode);
  }

  @GetMapping("/{id}/service-codes/{service-code}/clients")
  @ResponseStatus(code = HttpStatus.OK)
  public List<CacheClientsDto> getCacheClients(
      @PathVariable("id") long id,
      @PathVariable("service-code") String serviceCode) {
    return ensembleService.getCacheClients(id, serviceCode);
  }

  @GetMapping("/{id}/repl-service-codes/{service-code}/clients")
  @ResponseStatus(code = HttpStatus.OK)
  public List<CacheClientsDto> getReplicationCacheClients(
      @PathVariable("id") long id,
      @PathVariable("service-code") String serviceCode) {
    return ensembleService.getReplicationCacheClients(id, serviceCode);
  }

}
