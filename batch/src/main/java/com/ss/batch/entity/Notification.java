package com.ss.batch.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 예약테이블에서 예약 내용 가지고 수업 전에 알림 보내는 역할
// 카카오톡 메시지로 보내려면 UUID랑 Nofitication의 UUID를 이용해서 전송

@Entity
@Table(name = "notification")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification extends BaseEntity{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long notificationSeq; // 알람 순서
	private String uuid; // 카카오톡
	private NotificationEvent event; // 수업 전 알람을 보내기 위함.
	private String text; // 알림내용
	private boolean sent; // 발송 여부
	private LocalDateTime sentAt; // 발송 시간
}
