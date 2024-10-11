package com.ss.batch.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ss.batch.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity	, String>{

}
