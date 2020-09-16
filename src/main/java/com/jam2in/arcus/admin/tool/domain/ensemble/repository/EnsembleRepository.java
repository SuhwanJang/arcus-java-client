package com.jam2in.arcus.admin.tool.domain.ensemble.repository;

import com.jam2in.arcus.admin.tool.domain.ensemble.entity.EnsembleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EnsembleRepository extends JpaRepository<EnsembleEntity, Long> {

  @Query(
      "SELECT CASE WHEN COUNT(e) > 0 "
          + "THEN TRUE "
          + "ELSE FALSE "
          + "END "
          + "FROM EnsembleEntity e "
          + "WHERE LOWER(e.name) = LOWER(:name)")
  boolean existsByName(@Param("name") String name);

}
