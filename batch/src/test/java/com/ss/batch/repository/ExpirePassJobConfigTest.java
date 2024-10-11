package com.ss.batch.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.batch.runtime.JobInstance;

import org.junit.jupiter.api.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import com.ss.batch.TestBatchConfig;
import com.ss.batch.entity.PassEntity;
import com.ss.batch.entity.PassStatus;
import com.ss.batch.job.pass.ExpiredPassJobConfig;

@SpringBootTest
@SpringBatchTest
@ContextConfiguration(classes = {
		ExpiredPassJobConfig.class,
		TestBatchConfig.class
})
public class ExpirePassJobConfigTest {
	
	@Autowired
	private PassRepository repo;
	
	// 배치 작업을 테스트할 Job을 실행하고 그 결과를 가져오는데 사용된다.
	@Autowired
	private JobLauncherTestUtils jobLauncher;
	
	@Test
	public void test_expirePassStep() throws Exception {
		// given : 이미 사용중인 이용권들을 생성
		addPassEntities(10);
		
		// when : job을 가지고 와서 실행
		JobExecution jobExecution = jobLauncher.launchJob();
		
		// 위에서 실행된 job의 인스턴스를 가져온다.
		JobInstance jobInstance = jobExecution.getJobInstance();
		
		// then
		// 두개의 값이 같은지 확인하는 메서드7
		assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());
		
		// 내가 직접 설정한 job이 실행했는지 확인
		assertEquals("expiredPassJob", jobInstance.getJobName());
		
	}
	
	// 데이터 추가해서 만료시키는 메서드
	private void addPassEntities(int size) {
		final LocalDateTime now = LocalDateTime.now();
		final Random random = new Random();

		List<PassEntity> passEntities = new ArrayList<PassEntity>();
		for (int i = 0; i < size; ++i) {
			PassEntity passEntity = new PassEntity();
			passEntity.setPassSeq(1L);
			passEntity.setUser_id("A" + 10000 + i);
			passEntity.setStatus(PassStatus.PROGRESSED);
			passEntity.setRemaining_count(random.nextInt(11));
			passEntity.setStarted_at(now.minusDays(60));
			passEntity.setEnded_at(now.minusDays(1));
			passEntities.add(passEntity);
		}
		repo.saveAll(passEntities);		
	}
}
