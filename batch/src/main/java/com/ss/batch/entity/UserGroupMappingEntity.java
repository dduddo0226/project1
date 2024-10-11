package com.ss.batch.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "user_group_mapping")
@IdClass(UserGroupMappingId.class) // 복합키 선언
public class UserGroupMappingEntity extends BaseEntity {
	// 동일한 사용자가 그룹에 여러 사용자가 속할 수도 있고,
	// 하나의 사용자가 여러 그룹에 속할 수도 있음.
	@Id
	private String userGroupId; // 사용자 그룹을 구별하는 아이디값
	@Id
	private String userId; // 사용자 고유 아이디값
	private String userGroupName; // 그룹 이름
	private String description; // 설명
	
}
