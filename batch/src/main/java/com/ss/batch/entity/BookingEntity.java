package com.ss.batch.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "booking")
public class BookingEntity extends BaseEntity{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long bookingSeq; // 예약 순번
	private Long passSeq; // 어떤 이용권과 연결되어 있는 예약인지 확인
	private String userId; // 예약한 사람의 id
	@Enumerated(EnumType.STRING)
	private BookingStatus status; // 예약의 상태를 관리
	private boolean usedPass; // 이용권 사용 여부
	private boolean attended; // 예약 참석 여부 확인
	private LocalDateTime startedAt; // 시작시간
	private LocalDateTime endedAt; // 종료시간
	private LocalDateTime cancelledAt; // 취소시간
	
	// 예약한 사람의 메시지를 보내기 위해서 user 테이블과 조인
	// 여러 예약이 하나의 사용자에게 저장될 수 있다.
	@JoinColumn(name = "userId")
	@ManyToOne(fetch = FetchType.LAZY)
	private UserEntity userEntity;
}
