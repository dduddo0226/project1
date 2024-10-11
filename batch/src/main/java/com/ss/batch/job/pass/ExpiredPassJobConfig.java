package com.ss.batch.job.pass;

import java.time.LocalDateTime;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import org.springframework.batch.item.ItemProcessor;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ss.batch.entity.PassEntity;
import com.ss.batch.entity.PassStatus;

// 이용권이 만료되었을 때 배치 작업을 설정하는 클래스
@Configuration
public class ExpiredPassJobConfig {
	
	// 데이터를 한꺼번에 처리할 수 있는 사이즈
	private final  int CHUNK_SIZE = 1;

	// JOB을 생성하는 팩토리
	private final JobBuilderFactory jobBuilderFactory;
	
	// Step을 생성할 수 있는 팩토리(배치작업의 단계)
	private final StepBuilderFactory stepBuilderFactory;
	
	// JPA와 데이터베이스를 연결하고 관리하는 객체 생성
	private final EntityManagerFactory entityManagerFactory;

	public ExpiredPassJobConfig(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory,
			EntityManagerFactory entityManagerFactory) {
		super();
		this.jobBuilderFactory = jobBuilderFactory;
		this.stepBuilderFactory = stepBuilderFactory;
		this.entityManagerFactory = entityManagerFactory;
	}
	
	// JOB
	// 배치 작업을 말함. 여러 개의 step을 가질 수 있다.
	// 실행 시 여러 step이 순서대로 처리를 한다.
	@Bean
	public Job expiredPassJob() {
		// 배치 작성을 생성해서 이름을 저장
		return this.jobBuilderFactory.get("expiredPassJob")
				.start(expiredPassStep()) // step을 실행하는 메서드
				.build() // 실제 JOB을 생성한다.
				; 
		
	}
	
	// step
	// <PassEntity, PassEntity> 입력, 출력 데이터 타입
	// 첫번째 제네릭 : 데이터베이스에서 데이터를 읽어올 때 타입
	// 두번째 제네릭 : 데이터베이스에서 데이터를 처리하거나 
	// 처리 후 수정된 데이터나 추가된 데이터를 저장할 때 타입
	@Bean
	public Step expiredPassStep() {
		return this.stepBuilderFactory
				.get("expiredPassStep")
				.<PassEntity, PassEntity> chunk(CHUNK_SIZE)
				.reader(expirePassItemReader()) // 읽어오기
				.processor(expirePassesItemProcessor()) // 데이터를 처리
				.writer(expirepassitemWriter())
				.build()
				;
	}
	
	// JpaCursorItemReader
	// JpaPagingItemReader만 지원.
	// spring 4.3에 추가된 페이징 기법보다 높은 성능으로
	// 데이터 변경에 무관한 무결성 조회가 가능하다.
	@Bean
	@StepScope // step이 실행될 때마다 새로운 개게를 생성하도록 설정
	public JpaCursorItemReader<PassEntity> expirePassItemReader(){
		return new JpaCursorItemReaderBuilder<PassEntity>()
				.name("expirePassItemReader") // ItemReader 여러 개 중 리더를 구분
				.entityManagerFactory(entityManagerFactory) // JPA를 통해서 데이터베이스에 연결하고, 데이터베이스를 관리해줌.
				// 상태(status)가 진행중이면 종료일시(endedAt)가
				// 현재 시점보다 과거일 경우 만료 대상임을 알리기
				// JPQL 쿼리를 이용해서 데이터 저장
				.queryString("select p from PassEntity p where p.status = :status and p.ended_at <= :endedAt")
				// Map.of() : 자바에서 맵 객체를 자동으로 생성하는 것
				.parameterValues(Map.of("status", PassStatus.PROGRESSED, "endedAt", LocalDateTime.now()))
				.build()
				;
	}
	
	@Bean
    public ItemProcessor<PassEntity, PassEntity> expirePassesItemProcessor() {
        return passEntity -> {
            passEntity.setStatus(PassStatus.EXPIRED);
            passEntity.setExpired_at(LocalDateTime.now());;
            return passEntity;
        };
    }
	
	@Bean
	public JpaItemWriter<PassEntity> expirepassitemWriter(){
		JpaItemWriter<PassEntity> writer = new JpaItemWriter<PassEntity>();
		writer.setEntityManagerFactory(entityManagerFactory);
		return writer;
	}
}
