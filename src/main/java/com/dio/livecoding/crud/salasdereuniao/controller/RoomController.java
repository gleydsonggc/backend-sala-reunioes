package com.dio.livecoding.crud.salasdereuniao.controller;

import com.dio.livecoding.crud.salasdereuniao.exception.ResourceNotFoundException;
import com.dio.livecoding.crud.salasdereuniao.model.Room;
import com.dio.livecoding.crud.salasdereuniao.repository.RoomRepository;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/v1")
public class RoomController {

	@Autowired
	private RoomRepository roomRepository;

	@GetMapping("/rooms")
	public List<Room> getAllRooms() {
		return roomRepository.findAll();
	}

	@GetMapping("/rooms/{id}")
	public ResponseEntity<Room> getRoomById(@PathVariable(value = "id") Long roomId) {
		return roomRepository.findById(roomId)
				.map(ResponseEntity::ok)
				.orElseThrow(() -> new ResourceNotFoundException("Room not found :: " + roomId));
	}

	@PostMapping("/rooms")
	public ResponseEntity<Room> createRoom(@Valid @RequestBody Room room) {
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(roomRepository.save(room));
	}

	@PutMapping("/rooms/{id}")
	public ResponseEntity<Room> updateRoom(@PathVariable(value = "id") Long roomId,
										   @Valid @RequestBody Room roomDetails) {
		return roomRepository.findById(roomId)
				.map(room -> {
					room.setName(roomDetails.getName());
					room.setDate(roomDetails.getDate());
					room.setStartHour(roomDetails.getStartHour());
					room.setEndHour(roomDetails.getEndHour());
					return ResponseEntity.ok(roomRepository.save(room));
				})
				.orElseThrow(() -> new ResourceNotFoundException("Room not found :: " + roomId));
	}

	@DeleteMapping("/rooms/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteRoom(@PathVariable(value = "id") Long roomId) {
		roomRepository.findById(roomId).ifPresentOrElse(
				roomRepository::delete,
				() -> {
					throw new ResourceNotFoundException("Room not found for this id :: " + roomId);
				}
		);
	}

}

