package com.ASDC.backend.service;

import com.ASDC.backend.DummyData.DummyData;
import com.ASDC.backend.entity.*;
import com.ASDC.backend.repository.ActivityRepository;
import com.ASDC.backend.service.implementation.ActivityLoggingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ActivityLoggingServiceImplTest {

    @Mock
    private ActivityRepository activityRepository;

    @InjectMocks
    private ActivityLoggingServiceImpl activityLoggingServiceImpl;

    private Room room;
    private Activity activity;
    private Expense expense;
    private SettleUp settleUp;
    private User user;
    private DummyData dummyData;

    @BeforeEach
    void setUp() {
        dummyData = new DummyData();
        room = dummyData.createRoom();
        activity = dummyData.createActivity();
        expense = dummyData.createExpense();
        settleUp = dummyData.createSettleUp();
        user = dummyData.createUser();
    }

    @Test
    public void logExpenseActivity() {
        when(activityRepository.save(any(Activity.class))).thenReturn(activity);

        activityLoggingServiceImpl.logExpenseActivity(user, room, expense, Activity.ActivityType.Expense_ADD, "Added expense");

        verify(activityRepository, times(1)).save(any(Activity.class));
    }

    @Test
    public void logSettleUpActivity() {
        when(activityRepository.save(any(Activity.class))).thenReturn(activity);

        activityLoggingServiceImpl.logSettleUpActivity(user, room, settleUp, Activity.ActivityType.SETTLE_UP_Add, "Settle up added");

        verify(activityRepository, times(1)).save(any(Activity.class));
    }
}