package com.ASDC.backend.service.StrategyFactory;

import com.ASDC.backend.entity.Expense;
import com.ASDC.backend.entity.Room;
import com.ASDC.backend.service.Interface.RetrievalStrategy;
import com.ASDC.backend.service.PersistentServices.ExpensePersistentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class ActiveExpenseStrategy implements RetrievalStrategy<Expense> {

    private final ExpensePersistentService expensePersistentService;

    @Override
    public List<Expense> retrieveItems(Room room) {
        return expensePersistentService.getAllExpenseActive(room.getId());
    }
}
