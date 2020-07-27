package com.jam2in.arcus.admin.tool.domain.zookeeper.service;

import com.jam2in.arcus.admin.tool.domain.zookeeper.component.ZooKeeperFourLetterComponent;
import com.jam2in.arcus.admin.tool.domain.zookeeper.dto.ZooKeeperFourLetterConsDto;
import com.jam2in.arcus.admin.tool.domain.zookeeper.dto.ZooKeeperFourLetterMntrDto;
import com.jam2in.arcus.admin.tool.domain.zookeeper.dto.ZooKeeperFourLetterSrvrDto;
import com.jam2in.arcus.admin.tool.domain.zookeeper.entity.ZooKeeperEntity;
import com.jam2in.arcus.admin.tool.domain.zookeeper.repository.ZooKeeperRepository;
import com.jam2in.arcus.admin.tool.domain.zookeeper.util.ZooKeeperApiErrorUtil;
import com.jam2in.arcus.admin.tool.error.ApiErrorCode;
import com.jam2in.arcus.admin.tool.exception.BusinessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ZooKeeperService {

  private final ZooKeeperRepository zookeeperRepository;

  private final ZooKeeperFourLetterComponent fourLetterComponent;

  public ZooKeeperService(ZooKeeperRepository zookeeperRepository,
                          ZooKeeperFourLetterComponent fourLetterComponent) {
    this.zookeeperRepository = zookeeperRepository;
    this.fourLetterComponent = fourLetterComponent;
  }

  public ZooKeeperFourLetterSrvrDto getSrvr(long id) {
    try {
      return fourLetterComponent.getSrvr(getEntity(id).getAddress());
    } catch (Exception e) {
      throw new BusinessException(ZooKeeperApiErrorUtil.toErrorCode(e));
    }
  }

  public List<ZooKeeperFourLetterConsDto> getCons(long id) {
    try {
      return fourLetterComponent.getCons(getEntity(id).getAddress());
    } catch (Exception e) {
      throw new BusinessException(ZooKeeperApiErrorUtil.toErrorCode(e));
    }
  }

  public ZooKeeperFourLetterMntrDto getMntr(long id) {
    try {
      return fourLetterComponent.getMntr(getEntity(id).getAddress());
    } catch (Exception e) {
      throw new BusinessException(ZooKeeperApiErrorUtil.toErrorCode(e));
    }
  }

  private ZooKeeperEntity getEntity(long id) {
    return zookeeperRepository.findById(id)
        .orElseThrow(() -> new BusinessException(ApiErrorCode.ZOOKEEPER_NOT_FOUND));
  }

}
