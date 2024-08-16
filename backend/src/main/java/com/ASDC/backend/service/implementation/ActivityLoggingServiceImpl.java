package com.ASDC.backend.service.implementation;

import com.ASDC.backend.entity.*;
import com.ASDC.backend.repository.ActivityRepository;
import com.ASDC.backend.service.Interface.ActivityLoggingService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Service implementation for logging activity related to expenses and settle-ups.
 */
@Service
@RequiredArgsConstructor
public class ActivityLoggingServiceImpl implements ActivityLoggingService {

    private static final Logger logger = LogManager.getLogger(ActivityLoggingServiceImpl.class);

    private final ActivityRepository activityRepository;

    /**
     * Logs an expense-related activity.
     *
     * @param user        the user performing the activity
     * @param room        the room associated with the activity
     * @param expense     the expense related to the activity
     * @param activityType the type of activity being logged
     * @param details     additional details about the activity
     */
    @Override
    public void logExpenseActivity(User user, Room room, Expense expense, Activity.ActivityType activityType, String details) {
        logger.info("Logging expense activity for user: {}, room: {}, expense: {}, activityType: {}, details: {}",
                user.getId(), room.getId(), expense.getId(), activityType, details);
        Activity activity = Activity.builder()
                .expense(expense)
                .room(room)
                .type(activityType)
                .date(LocalDateTime.now())
                .datails(details)
                .build();
        logActivity(activity);
    }

    /**
     * Logs a settle-up-related activity.
     *
     * @param user        the user performing the activity
     * @param room        the room associated with the activity
     * @param settleup    the settle-up related to the activity
     * @param activityType the type of activity being logged
     * @param details     additional details about the activity
     */
    @Override
    public void logSettleUpActivity(User user, Room room, SettleUp settleup, Activity.ActivityType activityType, String details) {
        logger.info("Logging settle-up activity for user: {}, room: {}, settleUp: {}, activityType: {}, details: {}",
                user.getId(), room.getId(), settleup.getId(), activityType, details);
        Activity activity = Activity.builder()
                .settleUp(settleup)
                .room(room)
                .type(activityType)
                .date(LocalDateTime.now())
                .datails(details)
                .build();
        logActivity(activity);
    }

    /**
     * Logs an activity.
     *
     * @param activity the activity to be logged
     */
    private void logActivity(Activity activity) {
        logger.debug("Saving activity: {}", activity);
        activityRepository.save(activity);
    }
}