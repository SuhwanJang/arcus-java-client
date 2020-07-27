package com.jam2in.arcus.admin.tool.domain.kubernetes.repository;

import com.jam2in.arcus.admin.tool.domain.kubernetes.entity.KubernetesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface KubernetesRepository extends JpaRepository<KubernetesEntity, Long> {

  @Query(
      "SELECT CASE WHEN COUNT(e) > 0 "
          + "THEN TRUE "
          + "ELSE FALSE "
          + "END "
          + "FROM KubernetesEntity e "
          + "WHERE LOWER(e.name) = LOWER(:name)")
  boolean existsByName(@Param("name") String name);

  @Query(
      "SELECT CASE WHEN COUNT(e) > 0 "
          + "THEN TRUE "
          + "ELSE FALSE "
          + "END "
          + "FROM KubernetesEntity e "
          + "WHERE LOWER(e.address) = LOWER(:address)")
  boolean existsByAddress(@Param("address") String address);

}
