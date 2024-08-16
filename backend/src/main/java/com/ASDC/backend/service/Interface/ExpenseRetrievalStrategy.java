package com.ASDC.backend.service.Interface;

import com.ASDC.backend.entity.Expense;
import com.ASDC.backend.entity.Room;

import java.util.List;

public interface ExpenseRetrievalStrategy {
    List<Expense> retrieveExpenses(Room room);
}
