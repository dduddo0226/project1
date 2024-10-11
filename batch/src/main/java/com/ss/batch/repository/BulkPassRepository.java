package com.ss.batch.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ss.batch.entity.BulkPassEntity;
import com.ss.batch.entity.BulkPassStatus;

public interface BulkPassRepository extends JpaRepository<BulkPassEntity	, Long>{

	List<BulkPassEntity> findByStatusAndStartedAtGreaterThan(BulkPassStatus ready, LocalDateTime startAt);

}
