package com.ASDC.backend.service;

import com.ASDC.backend.DummyData.DummyData;
import com.ASDC.backend.dto.ResponseDTO.ActivityDTOResponse;
import com.ASDC.backend.dto.ResponseDTO.ActivityExpenseDTOResponse;
import com.ASDC.backend.dto.ResponseDTO.ActivitySettleUpDTOResponse;
import com.ASDC.backend.entity.*;
import com.ASDC.backend.mapper.ActivityMapper;
import com.ASDC.backend.repository.ActivityRepository;
import com.ASDC.backend.service.implementation.ActivityServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ActivityServiceImplTest {

    @Mock
    private ActivityRepository activityRepository;

    @Mock
    private ActivityMapper activityMapper;

    @InjectMocks
    private ActivityServiceImpl activityService;

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
    public void getAllForExpense_Success() {
        List<Activity> activities = Arrays.asList(activity);
        List<ActivityExpenseDTOResponse> dtoResponses = Arrays.asList(new ActivityExpenseDTOResponse());

        when(activityRepository.findAllForExpense(room.getId())).thenReturn(activities);
        when(activityMapper.toActivityExpenseDTOResponse(activity)).thenReturn(dtoResponses.get(0));

        List<ActivityExpenseDTOResponse> result = activityService.getAllForExpense(room);

        assertNotNull(result);
        assertEquals(dtoResponses.size(), result.size());
        assertEquals(dtoResponses.get(0), result.get(0));
        verify(activityRepository, times(1)).findAllForExpense(room.getId());
        verify(activityMapper, times(1)).toActivityExpenseDTOResponse(activity);
    }

    @Test
    public void getAllForSettleUp_Success() {
        List<Activity> activities = Arrays.asList(activity);
        List<ActivitySettleUpDTOResponse> dtoResponses = Arrays.asList(new ActivitySettleUpDTOResponse());

        when(activityRepository.findAllForSettleUp(room.getId())).thenReturn(activities);
        when(activityMapper.toActivitySettleUpDTOResponse(activity)).thenReturn(dtoResponses.get(0));

        List<ActivitySettleUpDTOResponse> result = activityService.getAllForSettleUp(room);

        assertNotNull(result);
        assertEquals(dtoResponses.size(), result.size());
        assertEquals(dtoResponses.get(0), result.get(0));
        verify(activityRepository, times(1)).findAllForSettleUp(room.getId());
        verify(activityMapper, times(1)).toActivitySettleUpDTOResponse(activity);
    }

    @Test
    public void getAll_Success() {
        List<Activity> activities = Arrays.asList(activity);
        List<ActivityDTOResponse> dtoResponses = Arrays.asList(new ActivityDTOResponse());

        when(activityRepository.findAll(room.getId())).thenReturn(activities);
        when(activityMapper.toActivityDTOResponse(activity)).thenReturn(dtoResponses.get(0));

        List<ActivityDTOResponse> result = activityService.getAll(room);

        assertNotNull(result);
        assertEquals(dtoResponses.size(), result.size());
        assertEquals(dtoResponses.get(0), result.get(0));
        verify(activityRepository, times(1)).findAll(room.getId());
        verify(activityMapper, times(1)).toActivityDTOResponse(activity);
    }
}