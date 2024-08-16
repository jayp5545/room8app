package com.ASDC.backend.service.implementation;

import com.ASDC.backend.dto.ResponseDTO.ActivityDTOResponse;
import com.ASDC.backend.dto.ResponseDTO.ActivityExpenseDTOResponse;
import com.ASDC.backend.dto.ResponseDTO.ActivitySettleUpDTOResponse;
import com.ASDC.backend.entity.Activity;
import com.ASDC.backend.entity.Room;
import com.ASDC.backend.mapper.ActivityMapper;
import com.ASDC.backend.repository.ActivityRepository;
import com.ASDC.backend.service.Interface.ActivityService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service implementation for managing activities related to expenses and settle-ups.
 */
@Service
@RequiredArgsConstructor
public class ActivityServiceImpl implements ActivityService {

    private static final Logger logger = LogManager.getLogger(ActivityServiceImpl.class);

    private final ActivityRepository activityRepository;
    private final ActivityMapper activityMapper;

    /**
     * Retrieves all expense-related activities for a given room.
     *
     * @param room the room for which activities are retrieved
     * @return a list of ActivityExpenseDTOResponse
     */
    @Override
    public List<ActivityExpenseDTOResponse> getAllForExpense(Room room) {
        logger.info("Retrieving all expense activities for room: {}", room.getId());
        List<ActivityExpenseDTOResponse> response = new ArrayList<>();
        List<Activity> activities = activityRepository.findAllForExpense(room.getId());

        for (Activity currActivity : activities) {
            response.add(activityMapper.toActivityExpenseDTOResponse(currActivity));
        }

        logger.debug("Expense activities retrieved: {}", response.size());
        return response;
    }

    /**
     * Retrieves all settle-up-related activities for a given room.
     *
     * @param room the room for which activities are retrieved
     * @return a list of ActivitySettleUpDTOResponse
     */
    @Override
    public List<ActivitySettleUpDTOResponse> getAllForSettleUp(Room room) {
        logger.info("Retrieving all settle-up activities for room: {}", room.getId());
        List<ActivitySettleUpDTOResponse> response = new ArrayList<>();
        List<Activity> activities = activityRepository.findAllForSettleUp(room.getId());

        for (Activity currActivity : activities) {
            response.add(activityMapper.toActivitySettleUpDTOResponse(currActivity));
        }

        logger.debug("Settle-up activities retrieved: {}", response.size());
        return response;
    }

    /**
     * Retrieves all activities for a given room.
     *
     * @param room the room for which activities are retrieved
     * @return a list of ActivityDTOResponse
     */
    @Override
    public List<ActivityDTOResponse> getAll(Room room) {
        logger.info("Retrieving all activities for room: {}", room.getId());
        List<ActivityDTOResponse> response = new ArrayList<>();
        List<Activity> activities = activityRepository.findAll(room.getId());

        for (Activity currActivity : activities) {
            response.add(activityMapper.toActivityDTOResponse(currActivity));
        }

        logger.debug("Total activities retrieved: {}", response.size());
        return response;
    }
}