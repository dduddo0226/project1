package com.ss.batch.repository;

import com.ss.batch.entity.PackageEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
// @ActiveProfiles("환경설정") : 테스트 환경을 만들어서 테스트하는 파일을 사용
// 서버설정(테스트할 때만 사용)
// application-test.properties
// application-test.yml
// dev : 개발 중에 사용하는 설정(로컬 데이터베이스, 디버그 모드)
// prod : 실제 서비스에서 사용하는 설정, 성능 최적화, 보안 관련 설정
// test : 테스트 환경을 나타내는 설정 파일

// 여러 개의 설정을 동시 설정 시
// @ActiveProfiles({"dev", "test"})
@ActiveProfiles
public class PackageRepositoryTest {

	@Autowired
	private PackageRepository repo;

	@Test // Junit은 이미 스프링 부트에서 제공하는 라이브러리
	public void test_save() {

		// given : 테스트를 위한 초기 데이터 설정
		PackageEntity entity = new PackageEntity();
		entity.setPackageName("바디 챌린지 PT 12주");
		entity.setPeriod(84);

		// when : 실제로 테스트할 작업 수행(데이터베이스 저장)
		repo.save(entity);

		// then : 예상 결과를 검증 (ID가 자동으로 생성됨)
		// 아이디가 자동으로 들어가면 null이 아닌 걸 확인할 수 있음.
		assertNotNull(entity.getPackSeq());

	}

	@Test
	public void test_findByCreateAtAfter() {

		// given
		// 현재 시간에서 1분 전 시간의 패키지 가져오기
		LocalDateTime dateTime = LocalDateTime.now().minusMinutes(1);

		PackageEntity pack1 = new PackageEntity();
		pack1.setPackageName("학생 전용 3개월");
		pack1.setPeriod(90);
		repo.save(pack1);

		PackageEntity pack2 = new PackageEntity();
		pack2.setPackageName("학생 전용 6개월");
		pack2.setPeriod(60);
		repo.save(pack2);

		// when : 특정 시간 이후에 생성된 패키지를 페이징 및
		// 정렬 조건에 맞춰서 조회
		// 최신 패키지를 조회 (내림차순 조회)
		PageRequest page = PageRequest.of(0, 1, Sort.by("packSeq").descending());
		List<PackageEntity> result = repo.findByCreateAtAfter(dateTime, page);

		// then : 결과 검증
		System.out.println("사이즈 반환 : " + result.size());
		assertEquals(1, result.size());

		// 시퀀스 아이디를 기준으로 조회
		assertEquals(pack2.getPackSeq(), result.get(0).getPackSeq());

	}
	
	
	@Test
	public void test_updateCountAndPeriod() {
		
		// given
		// 새로운 패키지 생성해서 바디프로필 이벤트 4개월
		// 데이터베이스 저장
		PackageEntity pack1 = new PackageEntity();
		pack1.setPackageName("바디프로필 이벤트 4개월");
		pack1.setPeriod(90);
		repo.save(pack1);
		
		// when : 업데이트 된 데이터를 다시 조회
		// 업데이트를 할 때 
		pack1.setCount(30);
		pack1.setPeriod(120);
//		repo.save(pack1);
		int updateRows = repo.updateCountAndPeriod(pack1.getPackSeq(), 30, 120);
		
//		System.out.println("pack1 의 시퀀스"+pack1.getPackSeq());
		System.out.println("실행한 행 : "+updateRows);
		
		final PackageEntity update = repo.findById(pack1.getPackSeq()).get();
		
		System.out.println(update.toString());
		
		// then
		assertEquals(30, update.getCount());
		assertEquals(120, update.getPeriod());
		
		// 업데이트된 행의 수가 1인지 확인
		assertEquals(1, updateRows);
		
	}	
	
	@Test
	public void test_delete() {
		// given : 초기 테스트 설정
		PackageEntity packageEntity = new PackageEntity();
		packageEntity.setPackageName("제거한 엔티티");
		packageEntity.setCount(1);
		
		PackageEntity newPackageEntity = repo.save(packageEntity);
		
		System.out.println("시퀀스 : "+newPackageEntity);
		// when : 실제 실행하는 구문
		repo.deleteById(newPackageEntity.getPackSeq());
		
		// then : 엔티티가 삭제 후 있는지 없는지 확인
		assertTrue(repo.findById(newPackageEntity.getPackSeq()).isEmpty());
	}
	
}
