package com.ASDC.backend.service.Interface;

import com.ASDC.backend.entity.*;

public interface ActivityLoggingService {
    void logExpenseActivity(User user, Room room, Expense expense, Activity.ActivityType activityType, String details);
    void logSettleUpActivity(User user, Room room, SettleUp settleup, Activity.ActivityType activityType, String details);
}
