package com.ss.batch.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ss.batch.entity.UserGroupMappingEntity;

public interface UserGroupMapping extends JpaRepository<UserGroupMappingEntity, Long>{


	List<UserGroupMappingEntity> findByUserGroupId(String userGroupId);
}
