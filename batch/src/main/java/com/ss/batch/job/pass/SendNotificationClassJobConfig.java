package com.ss.batch.job.pass;

import java.time.LocalDateTime;
import java.util.Map;

import javax.persistence.EntityManagerFactory;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ss.batch.entity.BookingEntity;
import com.ss.batch.entity.BookingStatus;
import com.ss.batch.entity.Notification;

@Configuration
public class SendNotificationClassJobConfig {
	private final int CHUNK_SIZE = 10;

	// JOB을 생성하는 팩토리
	private final JobBuilderFactory jobBuilderFactory;

	// Step을 생성할 수 있는 팩토리(배치작업의 단계)
	private final StepBuilderFactory stepBuilderFactory;

	// JPA와 데이터베이스를 연결하고 관리하는 객체 생성
	private final EntityManagerFactory entityManagerFactory;

	public SendNotificationClassJobConfig(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory,
			EntityManagerFactory entityManagerFactory) {
		super();
		this.jobBuilderFactory = jobBuilderFactory;
		this.stepBuilderFactory = stepBuilderFactory;
		this.entityManagerFactory = entityManagerFactory;
	}

	@Bean
	public Job sendNotificationClassJobConfig() {
		return this.jobBuilderFactory
				.get("sendNotificationClassJobConfig")
				.start()
				.next()
				.build();
	}
	
	// 세부기능(step)
	// 예약 정보 가지고 와서 알람 정보를 만들어주는 형태
	// input BookintEntity
	// output NotificationEntity
	@Bean
	public Step addNotificationStep() {
		return this.stepBuilderFactory
				.get("addNotificationStep")
				.<BookingEntity, Notification> chunk(CHUNK_SIZE)
				.reader(addNotificationItemReader())
				.processor()
				.writer()
				.build();
				
	}
	
	// 스텝에서 실질적으로 데이터를 읽어오는 메서드
	@Bean
	public JpaPagingItemReader<BookingEntity> addNotificationItemReader(){
		return new JpaPagingItemReaderBuilder()
				.name("addNotificationItemReader")
				.entityManagerFactory(entityManagerFactory)
				.pageSize(CHUNK_SIZE)
				// 상태가 준비중이면서 시작일시가 startAt이 10분 후 시작하는 예약을 찾아서 가져오기
				// 이때 유저에게 발송하기 위해 유저 조인해서 가져오기
				.queryString("select b from BookingEntity b join b.userEntity where b.status = :status and b.startedAt <= :startedAt order by b.bookingSeq")
				.parameterValues(Map.of("status", BookingStatus.READY,
								"startedAt", LocalDateTime.now().plusMinutes(10)))
				.build()
				;
	}
	
	
	
	

}
