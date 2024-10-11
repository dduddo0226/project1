package com.ss.batch.job.pass;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import com.ss.batch.entity.BulkPassEntity;
import com.ss.batch.entity.BulkPassStatus;
import com.ss.batch.entity.PassEntity;
import com.ss.batch.entity.UserGroupMappingEntity;
import com.ss.batch.modelmapper.PassModelMapper;
import com.ss.batch.repository.BulkPassRepository;
import com.ss.batch.repository.PassRepository;
import com.ss.batch.repository.UserGroupMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AddPassesTasklet implements Tasklet {

	private final PassRepository passRepository;
	private final BulkPassRepository bulkPassRepository;
	private final UserGroupMapping groupRepo;

	public AddPassesTasklet(PassRepository passRepository, BulkPassRepository bulkPassRepository,
			UserGroupMapping groupRepo) {
		this.passRepository = passRepository;
		this.bulkPassRepository = bulkPassRepository;
		this.groupRepo = groupRepo;
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		// 이용권 시작일은 하루 전 userGroup에 있는 각 사용자에게 이용권을 추가한다.

		final LocalDateTime startAt = LocalDateTime.now().minusDays(1);

		// 아직 처리되지 않은 대량의 이용권 목록을 불러온다.(읽기)
		List<BulkPassEntity> bulkPassEntities = bulkPassRepository
				.findByStatusAndStartedAtGreaterThan(BulkPassStatus.READY, startAt);
		// 각 대량의 이용권 정보를 돌면서 그룹 내에 속한 사용자들을 조회하고
		// 해당 사용자에게 이용권을 추가한다.

		// user group에 속한 userId들을 조회한다.

		// 각 userId로 이용권을 추가하는 내용

		int count = 0;
		
		for(BulkPassEntity bulkPassEntity : bulkPassEntities) {
			// 이걸 이용해서 사용자 목록 가져오기
			List<String> userIds = groupRepo.findByUserGroupId(bulkPassEntity.getUserGroupId())
			.stream() // 리스트나 컬렉션을 처리할 때 사용되는 메서드
			.map(UserGroupMappingEntity::getUserId)
			.collect(Collectors.toList());
			
			// Pass 추가 이후 상태 변경
			count += addPass(userIds, bulkPassEntity);
			bulkPassEntity.setStatus(BulkPassStatus.COMPLETE);
		}
		
		log.info("tasklet: execute 이용권:{}건 추가완료 startedAt: {}", count, startAt);

		return RepeatStatus.FINISHED;
	}
	
	// 이용권을 추가하는 메서드
	public int addPass(List<String> userIds, BulkPassEntity bulkPassEntity) {
		
		List<PassEntity> passEntities = new ArrayList<PassEntity>();
		
		for(String userId : userIds) {
			PassEntity passEntity = PassModelMapper.toPassEntity(userId, bulkPassEntity);
			passEntities.add(passEntity);
		}
		
		return passRepository.saveAll(passEntities).size();
	}

}
