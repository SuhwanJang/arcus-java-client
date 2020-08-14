package com.jam2in.arcus.admin.tool.domain.agent.repository;

import com.jam2in.arcus.admin.tool.domain.agent.entity.AdminAgentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminAgentRepository extends JpaRepository<AdminAgentEntity, Long> {

  Optional<AdminAgentEntity> findByIp(String ip);

}
