package com.ss.batch.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

// 대량 이용권
// 다수의 이용자에게 이용권을 지급하기 위한 테이블
@Table(name = "bulk_pass")
@Entity
@Data
public class BulkPassEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long bulkPassSqe; // 대량이용권 순번
	private Long packageSeq; // 패키지 순번
	private String userGroupId; // 사용자 그룹 ID
	@Enumerated(EnumType.STRING)
	private BulkPassStatus status; // 상태값
	private Integer count; // 이용권 수
	private LocalDateTime startedAt; // 시작일
	private LocalDateTime endedAt; // 종료일
}

// 엔티티끼리 데이터를 매칭시켜 저장할 때 자동으로 매칭시켜주는 라이브러리 mapstruct
// @Mapping(target = "remaining_count", source="bulkPassEntity.count")

//implementation 'org.mapstruct:mapstruct:1.4.2.Final' annotationProcessor 'org.mapstruct:mapstruct-processor:1.4.2.Final'