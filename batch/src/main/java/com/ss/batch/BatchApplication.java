package com.ss.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.*;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableBatchProcessing //spring batch 기능을 활성화
public class BatchApplication {
	//생성자 주입 final
	
	//JOB을 생성하는 팩토리
	private final JobBuilderFactory jobBuilderFactory;
	//Step을 생성하르 수 있는 팩토리
	private final StepBuilderFactory stepBuilderFactory;
	
	public BatchApplication(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
		this.jobBuilderFactory = jobBuilderFactory;
		this.stepBuilderFactory = stepBuilderFactory;
	}

	@Bean
	public Step passStep() {
		return this.stepBuilderFactory.get("passStep").tasklet(new Tasklet() {
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
				System.out.println("Execute PassStep!");
				return RepeatStatus.FINISHED;
			}
		}).build();
	}
	//Job
	// incrementer() 고유한 아이디를 생성하는 설정메서드
	// 매번 다른 ID를 Job실행시 생성해서 저장하는 역할
	// new RunIdIncrementer()
	@Bean
	public Job passJob() {
		return this.jobBuilderFactory.get("passJob").incrementer(new RunIdIncrementer()) //자동으로 고유한 값을 생성
				.start(passStep()).build();
	}


	public static void main(String[] args) {
		SpringApplication.run(BatchApplication.class, args);
	}

}
/*
	스프링 배치(spring batch)
		- 대량의 데이터를 처리하는 프레임 워크!
		- 대량의 데이터를 처리하는 작업을 의미한다. 자동화하여 시스템의 부하를 줄이고 효율적인 데이터 처리
		
	배치 프로그램
		- 대량의 데이터를 처리하는 작업을 자동화하는 프로그램
	
	스케줄러(Scheduler)
		-일정한 간격으로 반복적으로 수행되거나 특정 시간에 예약해 놓은 작업을 자동으로 실행해주는 시스템

	스프링 배치 처리하는 세 가지 단계
		- 1. 읽기 : 파일, 데이터베이스 등에서 데이터를 읽는다.
	   	  2. 쓰기 : 읽은 데이터를 필요한 형태로 변환하거나 처리한다.
	   	  3. 쓰기 : 처리 된 데이터를 파일이나 데이터베이스로 쓴다.
	   	  데이터가 없을 때까지 무한적으로 반복
	   	  
	위에서 데이터를 처리하다가 에러 발생하면 어떤 작업ㅇ르 하다가 에러가 났는지
	아니면 정상적으로 수행했는지 작업을 기록하는 테이블
	메타 데이터
	
	청크(Chunk)
	 - 데이터를 여러개의 묶음으로 처리
	 - 사이즈를 내가 설정할 수 있다. chunk(사이즈)
	 - 대량의 데이터를 처리할 때 사용한다.
	 - 메모리를 효율적으로 사용한다. 
	 
	태스크릿(Tasklet)
	 - 단일작업 처리하는데 샤용함
	 - 파일삭제, 데이터를 정리하는 단일작업
	 - 한번에 한가지 작업만 수행하고 처리한다.
	
	     
    JOB 안에 여러가자 step 을 이용해서 순서대로 처리할 수있다.
    JOB
     - 하나 이상의 step으로 구성되며 배치 처리의 최상위 단계
     
    JobInstance
     - 하나의 잡의 실행을 나타내는 인스턴스
     
    예) 이커미스 주문 처리 시스템 
      JOB: 하루동안 들어온 모든 주문을 처리하는 배치 작업
      step1: 주문 데이터베이스에서 미처리 주문 목록 읽기
      step2: 각 주문에 대해 결제 상태 확인 
      step3: 주문 상태에 따른 재고 업데이트 
      step4: 배송 정보 생성 및 고객에게 이메일 전송 
      step5:  결제 완료 및 배송 준비 상태로 주문 업데이트 
      
     대규모 이메일 뉴스전송 
      JOB : 뉴스레터 구독자들에게 이메일 전송
      step1: 구독자 데이터베이스에서 이메일 읽기 
      step2: 이메일 내용 생성
      step3: 이메일 전송 서비스 호출 API
      step4: 이메일 전송 결과 저장(성공,실패기록)
      step5: 실패시 재시도를 하거나 관리자에게 알림 전송
	
*/