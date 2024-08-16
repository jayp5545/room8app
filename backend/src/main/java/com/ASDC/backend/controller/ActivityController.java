package com.ASDC.backend.controller;

import com.ASDC.backend.Models.ExtractRequestInfo;
import com.ASDC.backend.dto.ResponseDTO.ActivityDTOResponse;
import com.ASDC.backend.dto.ResponseDTO.ActivityExpenseDTOResponse;
import com.ASDC.backend.dto.ResponseDTO.ActivitySettleUpDTOResponse;
import com.ASDC.backend.entity.Room;
import com.ASDC.backend.service.Interface.ActivityService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/**
 * Controller for handling activity-related API endpoints.
 */
@RestController
@RequestMapping("/api/v1/activity")
@RequiredArgsConstructor
public class ActivityController {

    private static final Logger logger = LogManager.getLogger(ActivityController.class);

    private final ActivityService activityService;
    private final ExtractRequestInfo extractRequestInfo;

    /**
     * Get all expense activities for the current room.
     *
     * @return a list of activity expense DTO responses
     */
    @GetMapping("/get/all/expense")
    public ResponseEntity<List<ActivityExpenseDTOResponse>> getExpense() {
        Room room = extractRequestInfo.getRoom();
        logger.info("Fetching all expense activities for room: {}", room.getId());
        List<ActivityExpenseDTOResponse> response = activityService.getAllForExpense(room);
        logger.info("Fetched {} expense activities for room: {}", response.size(), room.getId());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Get all settle-up activities for the current room.
     *
     * @return a list of activity settle-up DTO responses
     */
    @GetMapping("/get/all/settleup")
    public ResponseEntity<List<ActivitySettleUpDTOResponse>> getSettleUp() {
        Room room = extractRequestInfo.getRoom();
        logger.info("Fetching all settle-up activities for room: {}", room.getId());
        List<ActivitySettleUpDTOResponse> response = activityService.getAllForSettleUp(room);
        logger.info("Fetched {} settle-up activities for room: {}", response.size(), room.getId());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Get all activities for the current room.
     *
     * @return a list of activity DTO responses
     */
    @GetMapping("/get/all")
    public ResponseEntity<List<ActivityDTOResponse>> getAll() {
        Room room = extractRequestInfo.getRoom();
        logger.info("Fetching all activities for room: {}", room.getId());
        List<ActivityDTOResponse> response = activityService.getAll(room);
        logger.info("Fetched {} activities for room: {}", response.size(), room.getId());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}