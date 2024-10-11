package com.ss.batch.entity;

import javax.persistence.*;

import lombok.Data;

@Entity
@Table (name = "package")
@Data
public class PackageEntity extends BaseEntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long packSeq; // 패키지 ID
	
	private String packageName; // 패키지 이름
	private Integer count; // 횟수
	private Integer period; // 기간
	
	
}
