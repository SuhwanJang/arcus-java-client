package com.jam2in.arcus.admin.tool.domain.kubernetes.repository;

import com.jam2in.arcus.admin.tool.domain.kubernetes.entity.KubernetesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KubernetesRepository extends JpaRepository<KubernetesEntity, Long> {
}
