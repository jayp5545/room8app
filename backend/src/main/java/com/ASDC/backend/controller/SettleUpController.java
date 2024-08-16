package com.ASDC.backend.controller;

import com.ASDC.backend.dto.RequestDTO.SettleUpDTORequest;
import com.ASDC.backend.dto.ResponseDTO.SettleUpAmountDTOResponse;
import com.ASDC.backend.dto.ResponseDTO.SettleUpDTOResponse;
import com.ASDC.backend.entity.Room;
import com.ASDC.backend.entity.User;
import com.ASDC.backend.entity.UserRoomMapping;
import com.ASDC.backend.exception.CustomException;
import com.ASDC.backend.service.Interface.SettleUpService;
import com.ASDC.backend.Models.ExtractRequestInfo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for handling settle-up related API endpoints.
 */
@RestController
@RequestMapping("/api/v1/settleup")
@RequiredArgsConstructor
public class SettleUpController {

    private static final Logger logger = LogManager.getLogger(SettleUpController.class);

    private final SettleUpService settleUpService;
    private final ExtractRequestInfo extractRequestInfo;

    /**
     * Get all settle-up calculations for the current user and room.
     *
     * @return a list of settle-up amount DTO responses
     */
    @GetMapping("/calculations")
    public ResponseEntity<List<SettleUpAmountDTOResponse>> getAllSettleUpCalculations() {
        User user = extractRequestInfo.getUser();
        Room room = extractRequestInfo.getRoom();

        logger.info("Calculating settle-ups for user: {} in room: {}", user.getEmail(), room.getId());
        List<SettleUpAmountDTOResponse> response = settleUpService.calculateSettleUps(room, user);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Get all settle-ups of a specific type for the current user and room.
     *
     * @param type the type of settle-ups to fetch
     * @return a list of settle-up DTO responses
     */
    @GetMapping("/get/all/{type}")
    public ResponseEntity<List<SettleUpDTOResponse>> getAllSettleUps(@PathVariable(name = "type") String type) {
        User user = extractRequestInfo.getUser();
        Room room = extractRequestInfo.getRoom();

        logger.info("Fetching all settle-ups of type: {} for user: {} in room: {}", type, user.getEmail(), room.getId());
        List<SettleUpDTOResponse> response = settleUpService.findAllSettleUps(room, user, type);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Get a specific settle-up by its ID.
     *
     * @param settleUpID the ID of the settle-up to fetch
     * @return the settle-up DTO response
     */
    @GetMapping("/get/{id}")
    public ResponseEntity<SettleUpDTOResponse> getSettleUpById(@PathVariable(name = "id") int settleUpID) {
        validateSettleUpId(settleUpID);

        User user = extractRequestInfo.getUser();
        Room room = extractRequestInfo.getRoom();

        logger.info("Fetching settle-up with ID: {} for user: {} in room: {}", settleUpID, user.getEmail(), room.getId());
        SettleUpDTOResponse response = settleUpService.findSettleUpById(settleUpID, user, room);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Create a new settle-up.
     *
     * @param settleUpDTORequest the settle-up request DTO
     * @return the created settle-up DTO response
     */
    @PostMapping("/add")
    public ResponseEntity<SettleUpDTOResponse> createSettleUp(@Valid @RequestBody SettleUpDTORequest settleUpDTORequest) {
        User user = extractRequestInfo.getUser();
        Room room = extractRequestInfo.getRoom();
        UserRoomMapping userRoom = extractRequestInfo.getUserRoomMapping();

        logger.info("Creating settle-up for user: {} in room: {} with details: {}", user.getEmail(), room.getId(), settleUpDTORequest);
        SettleUpDTOResponse response = settleUpService.createSettleUp(settleUpDTORequest, user, room, userRoom);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Update an existing settle-up.
     *
     * @param settleUpDTORequest the settle-up request DTO
     * @return the updated settle-up DTO response
     */
    @PutMapping("/update")
    public ResponseEntity<SettleUpDTOResponse> updateSettleUp(@Valid @RequestBody SettleUpDTORequest settleUpDTORequest) {
        validateSettleUpId(settleUpDTORequest.getId());

        User user = extractRequestInfo.getUser();
        Room room = extractRequestInfo.getRoom();
        UserRoomMapping userRoom = extractRequestInfo.getUserRoomMapping();

        logger.info("Updating settle-up for user: {} in room: {} with details: {}", user.getEmail(), room.getId(), settleUpDTORequest);
        SettleUpDTOResponse response = settleUpService.updateSettleUp(settleUpDTORequest, user, room, userRoom);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Delete a specific settle-up by its ID.
     *
     * @param settleUpID the ID of the settle-up to delete
     * @return a success message
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteSettleUp(@PathVariable(name = "id") int settleUpID) {
        validateSettleUpId(settleUpID);

        User user = extractRequestInfo.getUser();
        Room room = extractRequestInfo.getRoom();

        logger.info("Deleting settle-up with ID: {} for user: {} in room: {}", settleUpID, user.getEmail(), room.getId());
        settleUpService.deleteSettleUp(settleUpID, user, room);

        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    /**
     * Validate the settle-up ID.
     *
     * @param settleUpID the settle-up ID to validate
     */
    private void validateSettleUpId(int settleUpID) {
        if (settleUpID <= 0) {
            logger.error("Invalid settle-up ID: {}", settleUpID);
            throw new CustomException("400", "Settle-Up ID must be greater than zero.");
        }
    }
}