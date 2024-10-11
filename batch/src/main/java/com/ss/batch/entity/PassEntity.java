package com.ss.batch.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

// 이용권 만료를 할 때
@Data
@Entity
public class PassEntity extends BaseEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pass_seq")
	private Long passSeq;
	private Long package_seq;
	private String user_id;
//  상태값으로 바꿈
//	private String status;
//	열거형 enum 쓸 때 데이터베이스에 저장할 때는 문자열로 저장될 수 있도록
//	어노테이션을 사용한다.
	@Enumerated(EnumType.STRING)
	private PassStatus status;
	private Integer remaining_count;
	private LocalDateTime started_at;
	private LocalDateTime ended_at;
	private LocalDateTime expired_at;
//	private LocalDateTime created_at;
//	private LocalDateTime modified_at;
}
