package com.ASDC.backend.service;

import com.ASDC.backend.DummyData.DummyData;
import com.ASDC.backend.dto.RequestDTO.ExpenseDTORequest;
import com.ASDC.backend.dto.ResponseDTO.ExpenseDTOResponse;
import com.ASDC.backend.entity.*;
import com.ASDC.backend.exception.CustomException;
import com.ASDC.backend.mapper.ExpenseMapper;
import com.ASDC.backend.service.Interface.UserService;
import com.ASDC.backend.service.implementation.ActivityLoggingServiceImpl;
import com.ASDC.backend.service.PersistentServices.ExpensePersistentService;
import com.ASDC.backend.service.implementation.ExpenseServiceImpl;
import com.ASDC.backend.service.PersistentServices.ParticipantPersistentService;
import com.ASDC.backend.service.StrategyFactory.ExpenseRetrievalStrategyFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExpenseServiceImplTest {

    private DummyData dummyData;
    private User user;
    private Room room;
    private UserRoomMapping userRoomMapping;
    private Expense expense;
    private ExpenseDTORequest expenseDTORequest;
    @InjectMocks
    private ExpenseServiceImpl expenseService;
    @Mock
    private ExpenseMapper expenseMapper;
    @Mock
    private ExpensePersistentService expensePersistentService;
    @Mock
    private ParticipantPersistentService participantPersistentService;
    @Mock
    private ActivityLoggingServiceImpl activityLoggingServiceImpl;
    @Mock
    private UserService userService;
    @Mock
    private ExpenseRetrievalStrategyFactory strategyFactory;

    @BeforeEach
    public void setUp() {
        dummyData = new DummyData();
        setDummyData();
    }


    private void setDummyData(){
        user = dummyData.createUser();
        room = dummyData.createRoom();
        userRoomMapping = dummyData.createUserRoomMapping();
        expense = dummyData.createExpense();
        expenseDTORequest = dummyData.createExpenseDTORequest();
    }


    @Test
    public void AddExpense_Success() {

        Expense savedExpense = new Expense();
        savedExpense.setId(1);

        List<Participant> participants = expense.getParticipants();
        participants.get(0).setId(1);
        participants.get(1).setId(2);

        ExpenseDTOResponse expenseDTOResponse = dummyData.createExpenseDTOResponse();

        when(userService.getUserByEmail(anyString())).thenReturn(user);
        when(expensePersistentService.saveExpense(any(Expense.class))).thenReturn(savedExpense);
        when(participantPersistentService.saveParticipants(anyList())).thenReturn(participants);
        doNothing().when(activityLoggingServiceImpl).logExpenseActivity(any(User.class), any(Room.class), any(Expense.class), any(Activity.ActivityType.class), anyString());
        when(expenseMapper.toExpenseDTOResponse(savedExpense)).thenReturn(expenseDTOResponse);

        ExpenseDTOResponse response = expenseService.createExpense(expenseDTORequest, user, room, userRoomMapping);

        assertNotNull(response);
        verify(expensePersistentService, times(1)).saveExpense(any(Expense.class));
        verify(participantPersistentService, times(1)).saveParticipants(anyList());
        assertEquals(response.getId(), 1);
    }

    @Test
    public void createExpense_Failure_InvalidUser() {
        when(userService.getUserByEmail(anyString())).thenThrow(new CustomException("400", "User doesn't exist!"));

        CustomException exception = assertThrows(CustomException.class, () -> {
            expenseService.createExpense(expenseDTORequest, user, room, userRoomMapping);
        });

        assertEquals("400", exception.getErrorCode());
        assertEquals("User with email " + expenseDTORequest.getPaidBy() + " does not exist.", exception.getErrorMessage());
    }


    /*@Test
    public void modifyExpense_Success() {
        Expense modifyExpense = dummyData.createExpense();
        modifyExpense.setParticipants(new ArrayList<>(Arrays.asList(dummyData.createParticipant(), dummyData.createParticipant())));

        when(userService.getUserByEmail(anyString())).thenReturn(user);
        when(expensePersistentService.getExpense(anyInt())).thenReturn(modifyExpense);
        when(expensePersistentService.saveExpense(any(Expense.class))).thenReturn(modifyExpense);
        when(expenseMapper.toExpenseDTOResponse(any(Expense.class))).thenReturn(dummyData.createExpenseDTOResponse());

        ExpenseDTOResponse response = expenseService.modifyExpense(expenseDTORequest, user, room, userRoomMapping);

        assertNotNull(response);
        verify(expensePersistentService, times(1)).saveExpense(any(Expense.class));
        verify(activityLoggingServiceImpl, times(1)).logExpenseActivity(any(User.class), any(Room.class), any(Expense.class), any(Activity.ActivityType.class), anyString());
    }*/

    @Test
    public void modifyExpense_Failure_DeletedExpense() {
        expense.setStatus(false);
        when(userService.getUserByEmail(anyString())).thenReturn(user);
        when(expensePersistentService.getExpense(anyInt())).thenReturn(expense);

        CustomException exception = assertThrows(CustomException.class, () -> {
            expenseService.modifyExpense(expenseDTORequest, user, room, userRoomMapping);
        });

        assertEquals("400", exception.getErrorCode());
        assertEquals("Deleted expense can't be modified!", exception.getErrorMessage());
    }

    @Test
    public void removeExpense_Success() {
        when(expensePersistentService.getExpense(anyInt())).thenReturn(expense);
        when(expensePersistentService.saveExpense(any(Expense.class))).thenReturn(expense);

        expenseService.removeExpense(expense.getId(), user, room);

        verify(expensePersistentService, times(1)).saveExpense(any(Expense.class));
        verify(activityLoggingServiceImpl, times(1)).logExpenseActivity(any(User.class), any(Room.class), any(Expense.class), any(Activity.ActivityType.class), anyString());
    }

    @Test
    public void fetchExpense_Success() {
        when(expensePersistentService.getExpense(anyInt())).thenReturn(expense);
        when(expenseMapper.toExpenseDTOResponse(any(Expense.class))).thenReturn(dummyData.createExpenseDTOResponse());

        ExpenseDTOResponse response = expenseService.fetchExpense(expense.getId());

        assertNotNull(response);
        verify(expensePersistentService, times(1)).getExpense(anyInt());
        verify(expenseMapper, times(1)).toExpenseDTOResponse(any(Expense.class));
    }

    @Test
    public void fetchExpense_Failure_NotFound() {
        when(expensePersistentService.getExpense(anyInt())).thenThrow(new CustomException("404", "Expense not found."));

        CustomException exception = assertThrows(CustomException.class, () -> {
            expenseService.fetchExpense(expense.getId());
        });

        assertEquals("404", exception.getErrorCode());
        assertEquals("Expense not found.", exception.getErrorMessage());
    }

    @Test
    public void listAllActiveExpenses_Success() {
        List<Expense> expenses = new ArrayList<>();
        expenses.add(expense);
        when(strategyFactory.getStrategy(anyString(), eq(Expense.class))).thenReturn(room -> expenses);
        when(expenseMapper.toExpenseDTOResponse(any(Expense.class))).thenReturn(dummyData.createExpenseDTOResponse());

        List<ExpenseDTOResponse> responses = expenseService.listExpenses(room, "active");

        assertNotNull(responses);
        assertEquals(1, responses.size());
        verify(strategyFactory, times(1)).getStrategy(anyString(), eq(Expense.class));
    }

    @Test
    public void listAllInActiveExpenses_Success() {
        List<Expense> expenses = new ArrayList<>();
        expenses.add(expense);
        when(strategyFactory.getStrategy(anyString(), eq(Expense.class))).thenReturn(room -> expenses);
        when(expenseMapper.toExpenseDTOResponse(any(Expense.class))).thenReturn(dummyData.createExpenseDTOResponse());

        List<ExpenseDTOResponse> responses = expenseService.listExpenses(room, "inactive");

        assertNotNull(responses);
        assertEquals(1, responses.size());
        verify(strategyFactory, times(1)).getStrategy(anyString(), eq(Expense.class));
    }

    @Test
    public void listAllExpenses_Success() {
        List<Expense> expenses = new ArrayList<>();
        expenses.add(expense);
        when(strategyFactory.getStrategy(anyString(), eq(Expense.class))).thenReturn(room -> expenses);
        when(expenseMapper.toExpenseDTOResponse(any(Expense.class))).thenReturn(dummyData.createExpenseDTOResponse());

        List<ExpenseDTOResponse> responses = expenseService.listExpenses(room, "all");

        assertNotNull(responses);
        assertEquals(1, responses.size());
        verify(strategyFactory, times(1)).getStrategy(anyString(), eq(Expense.class));
    }
}
