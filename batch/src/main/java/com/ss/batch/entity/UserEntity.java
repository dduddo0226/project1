package com.ss.batch.entity;

import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import com.vladmihalcea.hibernate.type.json.JsonStringType;

import lombok.Data;

@Data
@Entity
@Table(name = "user")
// @TypeDef : DB와의 타입 매핑시켜주는 어노테이션
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class UserEntity extends BaseEntity{
	@Id
	private String userId;
	private String userName;
	
	@Enumerated(EnumType.STRING)
	private UserStatus status;
	private String phone;
	// JSON형태로 저장된 데이터를 자바에서 사용하기 위한 구조
	@Type(type = "json")
	private Map<String, Object> meta; // 세부정보
	
	// 후반에 일괄적으로 고객에게 배포하기 위해서 카카오톡 메시지 보낼 때
	// UUID를 메타데이터에서 추출해서 저장
	
}
