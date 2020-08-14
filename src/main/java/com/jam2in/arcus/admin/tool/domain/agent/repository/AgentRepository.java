package com.jam2in.arcus.admin.tool.domain.agent.repository;

import com.jam2in.arcus.admin.tool.domain.agent.entity.AgentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AgentRepository extends JpaRepository<AgentEntity, Long> {

  Optional<AgentEntity> findByIp(String ip);

}
