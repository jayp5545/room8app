package com.ASDC.backend.service.StrategyFactory;

import com.ASDC.backend.entity.Expense;
import com.ASDC.backend.entity.SettleUp;
import com.ASDC.backend.exception.CustomException;
import com.ASDC.backend.service.Interface.RetrievalStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExpenseRetrievalStrategyFactory {

    private final ActiveExpenseStrategy activeExpenseStrategy;
    private final InActiveExpenseStrategy inactiveExpenseStrategy;
    private final AllExpenseStrategy allExpenseStrategy;

    private final ActiveSettleupStrategy activeSettleUpStrategy;
    private final InActiveSettleupStrategy inactiveSettleUpStrategy;
    private final AllSettleupStrategy allSettleUpStrategy;

    @Autowired
    public ExpenseRetrievalStrategyFactory(ActiveExpenseStrategy activeExpenseStrategy, InActiveExpenseStrategy inactiveExpenseStrategy, AllExpenseStrategy allExpenseStrategy, ActiveSettleupStrategy activeSettleUpStrategy, InActiveSettleupStrategy inactiveSettleUpStrategy, AllSettleupStrategy allSettleUpStrategy) {
        this.activeExpenseStrategy = activeExpenseStrategy;
        this.inactiveExpenseStrategy = inactiveExpenseStrategy;
        this.allExpenseStrategy = allExpenseStrategy;
        this.activeSettleUpStrategy = activeSettleUpStrategy;
        this.inactiveSettleUpStrategy = inactiveSettleUpStrategy;
        this.allSettleUpStrategy = allSettleUpStrategy;
    }

    public <T> RetrievalStrategy<T> getStrategy(String type, Class<T> clazz) {
        if (clazz == Expense.class) {
            switch (type) {
                case "active":
                    return (RetrievalStrategy<T>) activeExpenseStrategy;
                case "inactive":
                    return (RetrievalStrategy<T>) inactiveExpenseStrategy;
                case "all":
                    return (RetrievalStrategy<T>) allExpenseStrategy;
                default:
                    throw new CustomException("400", "Invalid expense type!");
            }
        } else if (clazz == SettleUp.class) {
            switch (type) {
                case "active":
                    return (RetrievalStrategy<T>) activeSettleUpStrategy;
                case "inactive":
                    return (RetrievalStrategy<T>) inactiveSettleUpStrategy;
                case "all":
                    return (RetrievalStrategy<T>) allSettleUpStrategy;
                default:
                    throw new CustomException("400", "Invalid settle-up type!");
            }
        } else {
            throw new CustomException("400", "Unsupported class type!");
        }
    }
}
