package com.jam2in.arcus.admin.tool.domain.ensemble.service;

import com.jam2in.arcus.admin.tool.domain.ensemble.dto.EnsembleDto;
import com.jam2in.arcus.admin.tool.domain.ensemble.dto.ZooKeeperStatsDto;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
public class ZooKeeperService {

  private final EnsembleService ensembleService;

  private Map<String, ZooKeeper> connections;

  public ZooKeeperService(EnsembleService ensembleService) {
    this.ensembleService = ensembleService;
  }

  public List<ZooKeeperStatsDto> getAll(int ensembleId) {
    EnsembleDto ensembleDto = ensembleService.get(ensembleId);

    Collection<String> zookeepers = ensembleDto.getZookeepers();






    return null;
  }

}
