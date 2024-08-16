package com.ASDC.backend.service.StrategyFactory;

import com.ASDC.backend.entity.Expense;
import com.ASDC.backend.entity.Room;
import com.ASDC.backend.service.Interface.RetrievalStrategy;
import com.ASDC.backend.service.PersistentServices.ExpensePersistentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AllExpenseStrategy implements RetrievalStrategy<Expense> {

    private final ExpensePersistentService expensePersistentService;

    @Autowired
    public AllExpenseStrategy(ExpensePersistentService expensePersistentService) {
        this.expensePersistentService = expensePersistentService;
    }

    @Override
    public List<Expense> retrieveItems(Room room) {
        return expensePersistentService.getAllExpense(room);
    }
}
