package com.ss.batch.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ss.batch.entity.PassEntity;

public interface PassRepository extends JpaRepository<PassEntity, Long>{
	
}
