package com.jam2in.arcus.admin.tool.domain.agent.repository;

import com.jam2in.arcus.admin.tool.domain.agent.entity.MemcachedOptionsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemcachedOptionsRepository extends JpaRepository<MemcachedOptionsEntity, String> {
}
