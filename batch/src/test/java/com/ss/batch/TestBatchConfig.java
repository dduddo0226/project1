package com.ss.batch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

// 스프링 테스트 환경에서 사용할 설정 파일을 지정
// 배치 설정 파일을 사용할 때
@Configuration
@EnableJpaAuditing
// Springboot의 자동 설정 기능을 활성화
// 데이터베이스 설정, 웹설정, 보안설정
@EnableAutoConfiguration
//	Springboot 대용량 데이터를 처리하는 배치 작업을 만들고 관리하는 프레임워크
// 실행할 수 있는 환경 구성
@EnableBatchProcessing
// 데이터베이스와 연결되는 클래스들이 어디 있는지 알려준다.
@EntityScan("com.ss.batch.entity")
// 데이터베이스에서 저장하고 가져오는 역할을 하는 클래스 위치도 알려준다.
@EnableJpaRepositories("com.ss.batch.repository")
// 데이터작업이 중간에 실패하지 않고 전부 성공적으로 처리되도록 관리해주는 기능 활성화
@EnableTransactionManagement
public class TestBatchConfig {
	
}
