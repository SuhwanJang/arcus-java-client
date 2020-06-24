package com.jam2in.arcus.admin.tool.domain.zookeeper.repository;

import com.jam2in.arcus.admin.tool.domain.zookeeper.entity.ZooKeeperEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ZooKeeperRepository extends JpaRepository<ZooKeeperEntity, Long> {
}
