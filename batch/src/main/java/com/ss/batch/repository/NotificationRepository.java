package com.ss.batch.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ss.batch.entity.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long>{

}
