package com.reservation.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
@jakarta.persistence.Table(name = "reservation_table")
public class Table {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reservation_table_generator")
	private long id;

	@Column(name = "capacity")
	private int guestCapacity;

	@Column(name = "description")
	private String description;

	public Table() {
	}

	public Table(int guestCapacity, String description) {
		this.guestCapacity = guestCapacity;
		this.description = description;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getGuestCapacity() {
		return guestCapacity;
	}

	public void setGuestCapacity(int guestCapacity) {
		this.guestCapacity = guestCapacity;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "Tutorial [id=" + id + ", guestCapacity=" + guestCapacity + ", description=" + description + "]";
	}

}
