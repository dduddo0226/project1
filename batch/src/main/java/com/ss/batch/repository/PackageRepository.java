package com.ss.batch.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ss.batch.entity.PackageEntity;

@Repository
public interface PackageRepository extends JpaRepository<PackageEntity, Long> {
	
	// 특정 시간 이후에 생성된 패키지를 페이징 조건으로 조회하는 메서드
	List<PackageEntity> findByCreateAtAfter(LocalDateTime dateTime, Pageable page);
	
	// 횟수, 기간을 업데이트하는 메서드
	@Modifying
	@Transactional
	@Query(value = "update PackageEntity p set p.count = :count, p.period = :period where p.packSeq = :packSeq")
	int updateCountAndPeriod(@Param("packSeq") Long packSeq, @Param("count") Integer count,
			@Param("period") Integer period);

}