package com.ASDC.backend.controller;

import com.ASDC.backend.Models.ExtractRequestInfo;
import com.ASDC.backend.dto.RequestDTO.JoinRoomDTORequest;
import com.ASDC.backend.dto.RequestDTO.RoomDTORequest;
import com.ASDC.backend.dto.ResponseDTO.JoinRoomDTOResponse;
import com.ASDC.backend.dto.ResponseDTO.RoomDTOResponse;
import com.ASDC.backend.dto.ResponseDTO.UserDTOResponse;
import com.ASDC.backend.entity.Room;
import com.ASDC.backend.entity.User;
import com.ASDC.backend.entity.UserRoomMapping;
import com.ASDC.backend.service.Interface.RoomService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for handling room-related API endpoints.
 */
@RestController
@RequestMapping("/api/v1/room")
@RequiredArgsConstructor
public class RoomController {

    private static final Logger logger = LogManager.getLogger(RoomController.class);

    private final RoomService roomService;
    private final ExtractRequestInfo extractRequestInfo;

    /**
     * Create a new room.
     *
     * @param roomDTORequest the room request DTO
     * @return the room response DTO
     */
    @PostMapping("/create")
    public ResponseEntity<RoomDTOResponse> createRoom(@RequestBody RoomDTORequest roomDTORequest) {
        logger.info("Creating room with details: {}", roomDTORequest);
        RoomDTOResponse room = roomService.createRoom(roomDTORequest);
        return ResponseEntity.ok(room);
    }

    /**
     * Get details of a room by its ID.
     *
     * @param roomID the room ID
     * @return the room response DTO
     */
    @GetMapping("/get/{id}")
    public ResponseEntity<RoomDTOResponse> getRoom(@PathVariable(name = "id") int roomID) {
        logger.info("Fetching room with ID: {}", roomID);
        RoomDTOResponse room = roomService.getRoomById(roomID);
        return ResponseEntity.ok(room);
    }

    /**
     * Update an existing room.
     *
     * @param roomDTORequest the room request DTO
     * @return the updated room response DTO
     */
    @PutMapping("/update")
    public ResponseEntity<RoomDTOResponse> updateRoom(@RequestBody RoomDTORequest roomDTORequest) {
        logger.info("Updating room with details: {}", roomDTORequest);
        RoomDTOResponse room = roomService.updateRoom(roomDTORequest);
        return ResponseEntity.ok(room);
    }

    /**
     * Delete a room by its ID.
     *
     * @param roomID the room ID
     * @return true if deletion was successful
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> deleteRoom(@PathVariable(name = "id") int roomID) {
        logger.info("Deleting room with ID: {}", roomID);
        roomService.deleteRoom(roomID);
        return ResponseEntity.ok(true);
    }

    /**
     * Join a room.
     *
     * @param joinRoomDTORequest the join room request DTO
     * @return the join room response DTO
     */
    @PostMapping("/join")
    public ResponseEntity<JoinRoomDTOResponse> joinRoom(@RequestBody JoinRoomDTORequest joinRoomDTORequest) {
        logger.info("Joining room with details: {}", joinRoomDTORequest);
        JoinRoomDTOResponse userRoomMapping = roomService.joinRoom(joinRoomDTORequest);
        return ResponseEntity.ok(userRoomMapping);
    }

    /**
     * Leave the current room.
     *
     * @return true if leaving the room was successful
     */
    @GetMapping("/leave")
    public ResponseEntity<Boolean> leaveRoom() {
        User user = extractRequestInfo.getUser();
        Room room = extractRequestInfo.getRoom();
        UserRoomMapping userRoom = extractRequestInfo.getUserRoomMapping();

        logger.info("User: {} leaving room: {}", user, room);
        roomService.leaveRoom(user, room, userRoom);
        return ResponseEntity.ok(true);
    }

    /**
     * Retrieves all users in the current room.
     *
     * @return the response entity with the list of all users
     */
    @GetMapping("/get/all/users")
    public ResponseEntity<List<UserDTOResponse>> getAllUsers() {
        Room room = extractRequestInfo.getRoom();
        logger.info("Retrieving all users for room ID: {}", room.getId());

        List<UserDTOResponse> response = roomService.getAllUsersByRoom(room.getId());
        return ResponseEntity.ok(response);
    }
}