package com.reservation.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reservation.exception.BadRequestException;
import com.reservation.exception.ResourceNotFoundException;
import com.reservation.model.Availability;
import com.reservation.model.Table;
import com.reservation.repository.AvailabilityRepository;
import com.reservation.repository.TableRepository;

@RestController
@CrossOrigin({"http://localhost:4200"})
@RequestMapping("/api")
public class AvailabilityController {

	@Autowired
	private TableRepository tableRepository;

	@Autowired
	private AvailabilityRepository availabilityRepository;

	@GetMapping("/tables/{tableId}/availabilities")
	public ResponseEntity<List<Availability>> getAllAvailabilitysByTableId(
			@PathVariable(value = "tableId") Long tableId) {
		if (!tableRepository.existsById(tableId)) {
			throw new ResourceNotFoundException("Not found Table with id = " + tableId);
		}

		List<Availability> availabilities = availabilityRepository.findByTableId(tableId);
		return new ResponseEntity<>(availabilities, HttpStatus.OK);
	}

	@GetMapping("/availabilities/{id}")
	public ResponseEntity<Availability> getAvailabilitysByTableId(@PathVariable(value = "id") Long id) {
		Availability availability = availabilityRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Not found Availability with id = " + id));

		return new ResponseEntity<>(availability, HttpStatus.OK);
	}

	@PostMapping("/tables/{tableId}/availabilities")
	public ResponseEntity<Availability> createAvailability(@PathVariable(value = "tableId") Long tableId,
			@RequestBody Availability availabilityRequest) {
		if (null == availabilityRequest || null == availabilityRequest.getAvailableTime()
				|| null == availabilityRequest.getGuestDetails() || "".equals(availabilityRequest.getGuestDetails())) {
			throw new BadRequestException("Please provide valid request");
		}
		Availability availability = tableRepository.findById(tableId).map(table -> {
			availabilityRequest.setTable(table);
			List<Availability> availabilities = availabilityRepository.findByTableIdAndAvailableTime(tableId,
					availabilityRequest.getAvailableTime());
			if (availabilities.isEmpty()) {
				availabilityRequest.setReservation(false);
				return availabilityRepository.save(availabilityRequest);
			} else {
				throw new BadRequestException(
						"Reservation Time exists for this table already " + availabilityRequest.getAvailableTime());
			}
		}).orElseThrow(() -> new ResourceNotFoundException("Not found Table with id = " + tableId));

		return new ResponseEntity<>(availability, HttpStatus.CREATED);
	}

	@PutMapping("/availabilities/{id}")
	public ResponseEntity<Availability> updateAvailability(@PathVariable("id") long id,
			@RequestBody Availability availabilityRequest) {
		Availability availability = availabilityRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("AvailabilityId " + id + "not found"));

		availability.setGuestDetails(availabilityRequest.getGuestDetails());
		availability.setReservation(availabilityRequest.isReservation());

		return new ResponseEntity<>(availabilityRepository.save(availability), HttpStatus.OK);
	}

	@DeleteMapping("/availabilities/{id}")
	public ResponseEntity<HttpStatus> deleteAvailability(@PathVariable("id") long id) {
		availabilityRepository.deleteById(id);

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@DeleteMapping("/tables/{tableId}/availabilities")
	public ResponseEntity<List<Availability>> deleteAllAvailabilitysOfTable(
			@PathVariable(value = "tableId") Long tableId) {
		if (!tableRepository.existsById(tableId)) {
			throw new ResourceNotFoundException("Not found Table with id = " + tableId);
		}

		availabilityRepository.deleteByTableId(tableId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@GetMapping("/availabilities/guests/{guests}")
	public ResponseEntity<List<Availability>> getAllAvailabilitysByGuests(@PathVariable(value = "guests") int guests) {

		if (!tableRepository.existsByGuestCapacity(guests)) {
			throw new ResourceNotFoundException("Not found Table for guests = " + guests);
		}

		List<Availability> availabilities = new ArrayList<>();
		List<Table> tables = tableRepository.getAllByGuestCapacity(guests);
		for (Table table : tables) {
			availabilities.addAll(availabilityRepository.findByTableIdAndReservation(table.getId(), false));
		}

		return new ResponseEntity<>(availabilities, HttpStatus.OK);
	}
	
//	@GetMapping("/allavailabilities")
//	public ResponseEntity<List<Availability>> getAllAvailableTables(@RequestBody Availability availabilityRequest) {
//
//		availabilityRepository.findAllAvailableTables(availabilityRequest.get);
//		return new ResponseEntity<>(availabilities, HttpStatus.OK);
//	}
}
