package com.ASDC.backend.service;

import com.ASDC.backend.DummyData.DummyData;
import com.ASDC.backend.entity.Expense;
import com.ASDC.backend.entity.Room;
import com.ASDC.backend.exception.CustomException;
import com.ASDC.backend.repository.ExpenseRepository;
import com.ASDC.backend.service.PersistentServices.ExpensePersistentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExpensePersistentServiceTest {

    @Mock
    private ExpenseRepository expenseRepository;

    @InjectMocks
    private ExpensePersistentService expensePersistentService;

    private Expense expense;
    private Room room;
    private DummyData dummyData;

    @BeforeEach
    void setUp() {
        dummyData = new DummyData();
        room = dummyData.createRoom();
        expense = dummyData.createExpense();
    }

    @Test
    public void saveExpense_Success() {
        when(expenseRepository.save(expense)).thenReturn(expense);

        Expense result = expensePersistentService.saveExpense(expense);

        assertNotNull(result);
        assertEquals(expense.getId(), result.getId());
        verify(expenseRepository, times(1)).save(expense);
    }

    @Test
    public void getExpense_Found() {
        when(expenseRepository.findActiveById(expense.getId())).thenReturn(Optional.of(expense));

        Expense result = expensePersistentService.getExpense(expense.getId());

        assertNotNull(result);
        assertEquals(expense.getId(), result.getId());
        verify(expenseRepository, times(1)).findActiveById(expense.getId());
    }

    @Test
    public void getExpense_NotFound() {
        when(expenseRepository.findActiveById(expense.getId())).thenReturn(Optional.empty());

        CustomException thrown = assertThrows(CustomException.class, () ->
                expensePersistentService.getExpense(expense.getId()));

        assertEquals("400", thrown.getErrorCode());
        assertEquals("Expense with ID " + expense.getId() + " does not exist.", thrown.getErrorMessage());
        verify(expenseRepository, times(1)).findActiveById(expense.getId());
    }

    @Test
    public void getAllExpense_Found() {
        when(expenseRepository.findAllByRoom(room)).thenReturn(Arrays.asList(expense));

        List<Expense> result = expensePersistentService.getAllExpense(room);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(expense.getId(), result.get(0).getId());
        verify(expenseRepository, times(1)).findAllByRoom(room);
    }

    @Test
    public void getAllExpense_Empty() {
        when(expenseRepository.findAllByRoom(room)).thenReturn(Arrays.asList());

        List<Expense> result = expensePersistentService.getAllExpense(room);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(expenseRepository, times(1)).findAllByRoom(room);
    }

    @Test
    public void getAllExpenseActive_Found() {
        when(expenseRepository.findAllByRoomActive(room.getId())).thenReturn(Arrays.asList(expense));

        List<Expense> result = expensePersistentService.getAllExpenseActive(room.getId());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(expense.getId(), result.get(0).getId());
        verify(expenseRepository, times(1)).findAllByRoomActive(room.getId());
    }

    @Test
    public void getAllExpenseActive_Empty() {
        when(expenseRepository.findAllByRoomActive(room.getId())).thenReturn(Arrays.asList());

        List<Expense> result = expensePersistentService.getAllExpenseActive(room.getId());

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(expenseRepository, times(1)).findAllByRoomActive(room.getId());
    }

    @Test
    public void getAllExpenseInActive_Found() {
        when(expenseRepository.findAllByRoomInActive(room.getId())).thenReturn(Arrays.asList(expense));

        List<Expense> result = expensePersistentService.getAllExpenseInActive(room.getId());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(expense.getId(), result.get(0).getId());
        verify(expenseRepository, times(1)).findAllByRoomInActive(room.getId());
    }

    @Test
    public void getAllExpenseInActive_Empty() {
        when(expenseRepository.findAllByRoomInActive(room.getId())).thenReturn(Arrays.asList());

        List<Expense> result = expensePersistentService.getAllExpenseInActive(room.getId());

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(expenseRepository, times(1)).findAllByRoomInActive(room.getId());
    }
}