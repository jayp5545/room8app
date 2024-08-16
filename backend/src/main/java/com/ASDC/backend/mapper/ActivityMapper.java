package com.ASDC.backend.mapper;

import com.ASDC.backend.dto.ResponseDTO.ActivityDTOResponse;
import com.ASDC.backend.dto.ResponseDTO.ActivityExpenseDTOResponse;
import com.ASDC.backend.dto.ResponseDTO.ActivitySettleUpDTOResponse;
import com.ASDC.backend.entity.Activity;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

/**
 * Mapper class for converting Activity entities to various DTO responses.
 */
@Component
@RequiredArgsConstructor
public class ActivityMapper {

    private static final Logger logger = LogManager.getLogger(ActivityMapper.class);

    private final RoomMapper roomMapper;
    private final ExpenseMapper expenseMapper;
    private final SettleUpMapper settleUpMapper;

    /**
     * Converts an Activity entity to an ActivityDTOResponse.
     *
     * @param activity the Activity entity
     * @return the ActivityDTOResponse
     */
    public ActivityDTOResponse toActivityDTOResponse(Activity activity) {
        if (activity == null) {
            logger.warn("Attempted to map null Activity to ActivityDTOResponse");
            return null;
        }

        ActivityDTOResponse activityDTOResponse = new ActivityDTOResponse();
        activityDTOResponse.setId(activity.getId());
        activityDTOResponse.setDate(activity.getDate());
        activityDTOResponse.setType(activity.getType());
        activityDTOResponse.setDetails(activity.getDatails());
        activityDTOResponse.setExpense(expenseMapper.toExpenseDTOResponse(activity.getExpense()));
        activityDTOResponse.setSettleUp(settleUpMapper.toSettleUpDTOResponse(activity.getSettleUp()));
        activityDTOResponse.setRoom(roomMapper.toRoomDTOResponse(activity.getRoom()));

        logger.info("Mapped Activity to ActivityDTOResponse: {}", activityDTOResponse);
        return activityDTOResponse;
    }

    /**
     * Converts an Activity entity to an ActivitySettleUpDTOResponse.
     *
     * @param activity the Activity entity
     * @return the ActivitySettleUpDTOResponse
     */
    public ActivitySettleUpDTOResponse toActivitySettleUpDTOResponse(Activity activity) {
        if (activity == null) {
            logger.warn("Attempted to map null Activity to ActivitySettleUpDTOResponse");
            return null;
        }

        ActivitySettleUpDTOResponse activitySettleUpDTOResponse = new ActivitySettleUpDTOResponse();
        activitySettleUpDTOResponse.setId(activity.getId());
        activitySettleUpDTOResponse.setDate(activity.getDate());
        activitySettleUpDTOResponse.setType(activity.getType());
        activitySettleUpDTOResponse.setDetails(activity.getDatails());
        activitySettleUpDTOResponse.setSettleup(settleUpMapper.toSettleUpDTOResponse(activity.getSettleUp()));
        activitySettleUpDTOResponse.setRoom(roomMapper.toRoomDTOResponse(activity.getRoom()));

        logger.info("Mapped Activity to ActivitySettleUpDTOResponse: {}", activitySettleUpDTOResponse);
        return activitySettleUpDTOResponse;
    }

    /**
     * Converts an Activity entity to an ActivityExpenseDTOResponse.
     *
     * @param activity the Activity entity
     * @return the ActivityExpenseDTOResponse
     */
    public ActivityExpenseDTOResponse toActivityExpenseDTOResponse(Activity activity) {
        if (activity == null) {
            logger.warn("Attempted to map null Activity to ActivityExpenseDTOResponse");
            return null;
        }

        ActivityExpenseDTOResponse activityExpenseDTOResponse = new ActivityExpenseDTOResponse();
        activityExpenseDTOResponse.setId(activity.getId());
        activityExpenseDTOResponse.setDate(activity.getDate());
        activityExpenseDTOResponse.setType(activity.getType());
        activityExpenseDTOResponse.setDetails(activity.getDatails());
        activityExpenseDTOResponse.setExpense(expenseMapper.toExpenseDTOResponse(activity.getExpense()));
        activityExpenseDTOResponse.setRoom(roomMapper.toRoomDTOResponse(activity.getRoom()));

        logger.info("Mapped Activity to ActivityExpenseDTOResponse: {}", activityExpenseDTOResponse);
        return activityExpenseDTOResponse;
    }
}