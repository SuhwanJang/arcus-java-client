package com.jam2in.arcus.admin.tool.domain.ensemble.service;

import com.jam2in.arcus.admin.tool.domain.ensemble.component.ZooKeeperFourLetterComponent;
import com.jam2in.arcus.admin.tool.domain.ensemble.dto.ZooKeeperFourLetterConsDto;
import com.jam2in.arcus.admin.tool.domain.ensemble.dto.ZooKeeperFourLetterMntrDto;
import com.jam2in.arcus.admin.tool.domain.ensemble.dto.ZooKeeperFourLetterSrvrDto;
import com.jam2in.arcus.admin.tool.domain.ensemble.entity.ZooKeeperEntity;
import com.jam2in.arcus.admin.tool.domain.ensemble.repository.ZooKeeperRepository;
import com.jam2in.arcus.admin.tool.domain.ensemble.util.ZooKeeperApiErrorUtil;
import com.jam2in.arcus.admin.tool.error.ApiErrorCode;
import com.jam2in.arcus.admin.tool.exception.BusinessException;
import org.springframework.stereotype.Service;

import java.util.Collection;

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

  public Collection<ZooKeeperFourLetterConsDto> getCons(long id) {
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
