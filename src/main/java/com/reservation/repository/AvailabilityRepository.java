package com.reservation.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.reservation.model.Availability;

import jakarta.transaction.Transactional;

public interface AvailabilityRepository extends JpaRepository<Availability, Long> {
	List<Availability> findByTableId(Long tableId);
	List<Availability> findByTableIdAndReservation(Long tableId, boolean isReservation);
	List<Availability> findByTableIdAndAvailableTime(Long tableId, LocalDateTime availableTime);

	@Transactional
	void deleteByTableId(long tableId);

	void findAllByTableId(long id);
	
	@Query(value = "select * from availability a inner join reservation_table rt ON rt.id = a.table_id where rt.capacity = ?1 and reservation = ?2", nativeQuery = true)
	void findAllAvailableTables(Availability availabilityRequest);
}
