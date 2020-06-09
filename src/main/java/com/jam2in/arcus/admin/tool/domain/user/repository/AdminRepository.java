package com.jam2in.arcus.admin.tool.domain.user.repository;

import com.jam2in.arcus.admin.tool.domain.user.entity.AdminEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<AdminEntity, Long> {
}
