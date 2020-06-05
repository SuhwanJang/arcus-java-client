package com.jam2in.arcus.admin.tool.domain.ensemble.repository;

import com.jam2in.arcus.admin.tool.domain.ensemble.entity.EnsembleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnsembleRepository extends JpaRepository<EnsembleEntity, Long> {
}