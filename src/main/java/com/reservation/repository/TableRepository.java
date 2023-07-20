package com.reservation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.reservation.model.Table;

public interface TableRepository extends JpaRepository<Table, Long> {
//	List<Table> findByPublished(boolean published);
//
//	List<Table> findByTitleContaining(String title);

	boolean existsByGuestCapacity(int guests);

	List<Table> getAllByGuestCapacity(int guests);
}
