package com.ASDC.backend.service;

import com.ASDC.backend.DummyData.DummyData;
import com.ASDC.backend.entity.Expense;
import com.ASDC.backend.entity.Room;
import com.ASDC.backend.service.PersistentServices.ExpensePersistentService;
import com.ASDC.backend.service.StrategyFactory.InActiveExpenseStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class InActiveExpenseStrategyTest {

    @Mock
    private ExpensePersistentService expensePersistentService;

    @InjectMocks
    private InActiveExpenseStrategy inactiveExpenseStrategy;

    private Room room;
    private DummyData dummyData;

    @BeforeEach
    public void setUp() {
        dummyData = new DummyData();
        room = dummyData.createRoom();
    }

    @Test
    public void retrieveItems_ReturnsInactiveExpenses() {
        List<Expense> expenses = List.of(new Expense(), new Expense());
        when(expensePersistentService.getAllExpenseInActive(room.getId())).thenReturn(expenses);

        List<Expense> result = inactiveExpenseStrategy.retrieveItems(room);
        assertNotNull(result);
        assertEquals(2, result.size());
    }
}