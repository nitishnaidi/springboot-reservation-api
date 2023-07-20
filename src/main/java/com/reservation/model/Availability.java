package com.reservation.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@jakarta.persistence.Table(name = "availability")
public class Availability {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "availability_generator")
	private Long id;

	@Column(name = "available_time")
	private LocalDateTime availableTime;

	@Column(name = "reservation")
	private boolean reservation = false;
	
	@Column(name = "guest_details")
	private String guestDetails;

//  @ManyToOne(fetch = FetchType.EAGER, optional = false)
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "table_id", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
	private Table table;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getAvailableTime() {
		return availableTime;
	}

	public void setAvailableTime(LocalDateTime availableTime) {
		this.availableTime = availableTime;
	}

	public boolean isReservation() {
		return reservation;
	}

	public void setReservation(boolean reservation) {
		this.reservation = reservation;
	}
	
	public String getGuestDetails() {
		return guestDetails;
	}

	public void setGuestDetails(String guestDetails) {
		this.guestDetails = guestDetails;
	}

	public Table getTable() {
		return table;
	}

	public void setTable(Table table) {
		this.table = table;
	}

}
