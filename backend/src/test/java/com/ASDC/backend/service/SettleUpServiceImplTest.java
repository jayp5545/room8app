package com.ASDC.backend.service;

import com.ASDC.backend.DummyData.DummyData;
import com.ASDC.backend.Models.SettleUpAmountDTOProjection;
import com.ASDC.backend.dto.RequestDTO.SettleUpDTORequest;
import com.ASDC.backend.dto.ResponseDTO.ExpenseDTOResponse;
import com.ASDC.backend.dto.ResponseDTO.SettleUpAmountDTOResponse;
import com.ASDC.backend.dto.ResponseDTO.SettleUpDTOResponse;
import com.ASDC.backend.dto.ResponseDTO.UserDTOResponse;
import com.ASDC.backend.entity.*;
import com.ASDC.backend.exception.CustomException;
import com.ASDC.backend.mapper.SettleUpMapper;
import com.ASDC.backend.service.Interface.ExpenseService;
import com.ASDC.backend.service.Interface.UserRoomService;
import com.ASDC.backend.service.Interface.UserService;
import com.ASDC.backend.service.PersistentServices.SettleupPersistentService;
import com.ASDC.backend.service.StrategyFactory.ExpenseRetrievalStrategyFactory;
import com.ASDC.backend.service.implementation.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(MockitoExtension.class)
public class SettleUpServiceImplTest {

    /*private DummyData dummyData;
    private User user;
    private Room room;
    private SettleUp settleUp;
    private SettleUpDTORequest settleUpDTORequest;
    private SettleUpDTOResponse settleUpDTOResponse;
    @Mock
    private SettleupPersistentService settleupPersistentService;
    @Mock
    private ExpenseService expenseService;
    @Mock
    private ActivityLoggingServiceImpl activityLoggingServiceImpl;
    @InjectMocks
    private SettleUpServiceImpl settleUpService;
    @Mock
    private ExpenseRetrievalStrategyFactory strategyFactory;
    @Mock
    private SettleupCalculationImpl settleupCalculationImpl;
    private UserRoomMapping userRoomMapping;
    @Mock
    private UserService userService;
    @Mock
    private SettleUpMapper settleUpMapper;
    @Mock
    private UserRoomService userRoomService;

    @BeforeEach
    public void setUp() {
        dummyData = new DummyData();
        setDummyData();
    }

    private void setDummyData(){
        user = dummyData.createUser();
        room = dummyData.createRoom();
        userRoomMapping = dummyData.createUserRoomMapping();
        settleUp = dummyData.createSettleUp();
        settleUpDTORequest = dummyData.createSettleUpDTORequest();
        settleUpDTOResponse = dummyData.createSettleUpDTOResponse();
    }

    @Test
    public void testCreateSettleUp() {
        when(userService.getUserByEmail(settleUpDTORequest.getTo())).thenReturn(user);
        when(userRoomService.getUserRoom(user)).thenReturn(userRoomMapping);
        when(settleupPersistentService.saveSettleUp(any(SettleUp.class))).thenReturn(dummyData.createSettleUp());
        when(settleUpMapper.toSettleUpDTOResponse(any(SettleUp.class))).thenReturn(dummyData.createSettleUpDTOResponse());

        SettleUpDTOResponse response = settleUpService.createSettleUp(settleUpDTORequest, user, room, userRoomMapping);

        assertNotNull(response);
        assertEquals(100.0, response.getAmount());
        verify(settleupPersistentService, times(1)).saveSettleUp(any(SettleUp.class));
        verify(activityLoggingServiceImpl, times(1)).logSettleUpActivity(any(User.class), any(Room.class), any(SettleUp.class), any(Activity.ActivityType.class), anyString());
    }

    @Test
    public void testCreateSettleUp_UserNotFound() {
        when(userService.getUserByEmail(anyString())).thenThrow(new CustomException("400", "User not found"));

        CustomException exception = assertThrows(CustomException.class, () -> {
            settleUpService.createSettleUp(settleUpDTORequest, user, room, dummyData.createUserRoomMapping());
        });

        assertEquals("400", exception.getErrorCode());
        assertEquals("User not found", exception.getErrorMessage());
    }

    @Test
    public void testCreateSettleUp_UserRoomMappingNotFound() {
        when(userService.getUserByEmail(settleUpDTORequest.getTo())).thenReturn(user);
        when(userRoomService.getUserRoom(user)).thenThrow(new CustomException("400", "User room mapping not found"));

        CustomException exception = assertThrows(CustomException.class, () -> {
            settleUpService.createSettleUp(settleUpDTORequest, user, room, dummyData.createUserRoomMapping());
        });

        assertEquals("400", exception.getErrorCode());
        assertEquals("User room mapping not found", exception.getErrorMessage());
    }

    @Test
    public void testUpdateSettleUp_Success() {

        User recevier = dummyData.createUser();

        when(userService.getUserByEmail(settleUpDTORequest.getTo())).thenReturn(recevier);
        when(settleupPersistentService.getSettleUp(settleUpDTORequest.getId())).thenReturn(settleUp);
        when(settleupPersistentService.saveSettleUp(any(SettleUp.class))).thenReturn(settleUp);
        when(settleUpMapper.toSettleUpDTOResponse(any(SettleUp.class))).thenReturn(settleUpDTOResponse);

        SettleUpDTOResponse response = settleUpService.updateSettleUp(settleUpDTORequest, user, room, dummyData.createUserRoomMapping());

        assertNotNull(response);
        assertEquals(100.0, response.getAmount());
        verify(settleupPersistentService, times(1)).saveSettleUp(any(SettleUp.class));
        verify(activityLoggingServiceImpl, times(1)).logSettleUpActivity(any(User.class), any(Room.class), any(SettleUp.class), any(Activity.ActivityType.class), anyString());
    }

    @Test
    public void testUpdateSettleUp_NotFound() {
        User recevier = dummyData.createUser();

        when(userService.getUserByEmail(settleUpDTORequest.getTo())).thenReturn(recevier);
        when(settleupPersistentService.getSettleUp(settleUpDTORequest.getId())).thenThrow(new CustomException("400","No settle up found to update!"));

        CustomException exception = assertThrows(CustomException.class, () -> {
            settleUpService.updateSettleUp(settleUpDTORequest, user, room, dummyData.createUserRoomMapping());
        });

        assertEquals("400", exception.getErrorCode());
        assertEquals("No settle up found to update!", exception.getErrorMessage());
    }

    @Test
    public void testDeleteSettleUp_Success() {
        when(settleupPersistentService.getSettleUp(settleUpDTORequest.getId())).thenReturn(settleUp);
        when(settleupPersistentService.saveSettleUp(any(SettleUp.class))).thenReturn(settleUp);

        settleUpService.deleteSettleUp(1, user, room);

        verify(settleupPersistentService, times(1)).getSettleUp(settleUpDTORequest.getId());
        verify(settleupPersistentService, times(1)).saveSettleUp(any(SettleUp.class));
    }

    @Test
    public void testDeleteSettleUp_NotFound() {
        when(settleupPersistentService.getSettleUp(settleUpDTORequest.getId())).thenThrow(new CustomException("400", "No settle up found to delete!"));

        CustomException exception = assertThrows(CustomException.class, () -> {
            settleUpService.deleteSettleUp(1, user, room);
        });

        assertEquals("400", exception.getErrorCode());
        assertEquals("No settle up found to delete!", exception.getErrorMessage());
    }

    @Test
    public void testGetSettleUp_Success() {
        when(settleupPersistentService.getSettleUp(settleUpDTORequest.getId())).thenReturn(settleUp);
        when(settleUpMapper.toSettleUpDTOResponse(any(SettleUp.class))).thenReturn(settleUpDTOResponse);

        SettleUpDTOResponse response = settleUpService.findSettleUpById(1, user, room);

        assertNotNull(response);
        assertEquals(100.0, response.getAmount());
        verify(settleupPersistentService, times(1)).getSettleUp(settleUpDTORequest.getId());
    }

    @Test
    public void testGetSettleUp_NotFound() {
        when(settleupPersistentService.getSettleUp(anyInt())).thenThrow(new CustomException("400", "No settle up found!"));

        CustomException exception = assertThrows(CustomException.class, () -> {
            settleUpService.findSettleUpById(2, user, room);
        });

        assertEquals("400", exception.getErrorCode());
        assertEquals("No settle up found!", exception.getErrorMessage());
    }


    @Test
    public void testGetAllActiveSettleUp_Active() {
        List<SettleUp> settle = new ArrayList<>();
        settle.add(settleUp);
        when(strategyFactory.getStrategy(anyString(), eq(SettleUp.class))).thenReturn(room -> settle);
        when(settleUpMapper.toSettleUpDTOResponse(any(SettleUp.class))).thenReturn(dummyData.createSettleUpDTOResponse());

        List<SettleUpDTOResponse> responses = settleUpService.findAllSettleUps(room, user, "active");

        assertNotNull(responses);
        assertEquals(1, responses.size());
        verify(strategyFactory, Mockito.times(1)).getStrategy(anyString(), eq(SettleUp.class));
    }

    @Test
    public void testGetAllInActiveSettleUp_Active() {
        List<SettleUp> settle = new ArrayList<>();
        settle.add(settleUp);
        when(strategyFactory.getStrategy(anyString(), eq(SettleUp.class))).thenReturn(room -> settle);
        when(settleUpMapper.toSettleUpDTOResponse(any(SettleUp.class))).thenReturn(dummyData.createSettleUpDTOResponse());

        List<SettleUpDTOResponse> responses = settleUpService.findAllSettleUps(room, user, "inactive");

        assertNotNull(responses);
        assertEquals(1, responses.size());
        verify(strategyFactory, Mockito.times(1)).getStrategy(anyString(), eq(SettleUp.class));
    }

    @Test
    public void testGetAllSettleUp_Active() {
        List<SettleUp> settle = new ArrayList<>();
        settle.add(settleUp);
        when(strategyFactory.getStrategy(anyString(), eq(SettleUp.class))).thenReturn(room -> settle);
        when(settleUpMapper.toSettleUpDTOResponse(any(SettleUp.class))).thenReturn(dummyData.createSettleUpDTOResponse());

        List<SettleUpDTOResponse> responses = settleUpService.findAllSettleUps(room, user, "all");

        assertNotNull(responses);
        assertEquals(1, responses.size());
        verify(strategyFactory, Mockito.times(1)).getStrategy(anyString(), eq(SettleUp.class));
    }

    @Test
    public void testCalculateSettleUps_Success() {

        List<ExpenseDTOResponse> expenses = new ArrayList<>();
        expenses.add(dummyData.createExpenseDTOResponse());

        List<SettleUpDTOResponse> settleUps = new ArrayList<>();
        settleUps.add(dummyData.createSettleUpDTOResponse());

        UserDTOResponse sender = dummyData.createUserDTOResponse();
        sender.setId(1L);

        UserDTOResponse receiver = dummyData.createUserDTOResponse();
        receiver.setId(3L);

        SettleUpAmountDTOResponse dtoResponse = new SettleUpAmountDTOResponse();
        dtoResponse.setFrom(sender);
        dtoResponse.setTo(receiver);
        dtoResponse.setAmount(50.0);
        dtoResponse.setStatus(true);

        List<SettleUpAmountDTOResponse> calculatedSettleUps = new ArrayList<>();
        calculatedSettleUps.add(dtoResponse);

        SettleUpAmountDTOProjection dtoProjection = mock(SettleUpAmountDTOProjection.class);
        when(expenseService.listExpenses(eq(room), eq("active"))).thenReturn(expenses);
        when(settleupPersistentService.findSettleUpBySum(eq(room.getId()))).thenReturn(Arrays.asList(dtoProjection));
        when(settleupCalculationImpl.calculate(eq(room), eq(user), eq(expenses), eq(Arrays.asList(dtoProjection)))).thenReturn(calculatedSettleUps);

        List<SettleUpAmountDTOResponse> result = settleUpService.calculateSettleUps(room, user);

        assertNotNull(result);
        assertEquals(1, result.size());

        SettleUpAmountDTOResponse resultDto = result.get(0);
        assertEquals(1, resultDto.getFrom().getId());
        assertEquals(3, resultDto.getTo().getId());
        assertEquals(50.0, resultDto.getAmount(), 0.01);

        verify(expenseService, times(1)).listExpenses(eq(room), eq("active"));
        verify(settleupPersistentService, times(1)).findSettleUpBySum(eq(room.getId()));
        verify(settleupCalculationImpl, times(1)).calculate(eq(room), eq(user), eq(expenses), eq(Arrays.asList(dtoProjection)));
    }
*/

}