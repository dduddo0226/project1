package com.ss.batch.entity;

import java.io.Serializable;

import javax.persistence.Id;

// Serializable : 두 개의 키를 식별할 수 있도록 직렬화가 필요함.
public class UserGroupMappingId implements Serializable{
	
	private String userGroupId; // 사용자 그룹을 구별하는 아이디값
	private String userId; // 사용자 고유 아이디값
}
