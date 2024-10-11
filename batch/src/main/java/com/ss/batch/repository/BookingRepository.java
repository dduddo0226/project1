package com.ss.batch.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ss.batch.entity.BookingEntity;

public interface BookingRepository extends JpaRepository<BookingEntity, Long> {

}
