package com.ASDC.backend.service;

import com.ASDC.backend.entity.Expense;
import com.ASDC.backend.entity.SettleUp;
import com.ASDC.backend.exception.CustomException;
import com.ASDC.backend.service.StrategyFactory.ActiveExpenseStrategy;
import com.ASDC.backend.service.StrategyFactory.AllExpenseStrategy;
import com.ASDC.backend.service.StrategyFactory.InActiveExpenseStrategy;
import com.ASDC.backend.service.StrategyFactory.ExpenseRetrievalStrategyFactory;
import com.ASDC.backend.service.StrategyFactory.ActiveSettleupStrategy;
import com.ASDC.backend.service.StrategyFactory.AllSettleupStrategy;
import com.ASDC.backend.service.StrategyFactory.InActiveSettleupStrategy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class ExpenseRetrievalStrategyFactoryTest {

    @Mock
    private ActiveExpenseStrategy activeExpenseStrategy;

    @Mock
    private InActiveExpenseStrategy inactiveExpenseStrategy;

    @Mock
    private AllExpenseStrategy allExpenseStrategy;

    @Mock
    private ActiveSettleupStrategy activeSettleupStrategy;

    @Mock
    private InActiveSettleupStrategy inactiveSettleupStrategy;

    @Mock
    private AllSettleupStrategy allSettleupStrategy;

    @InjectMocks
    private ExpenseRetrievalStrategyFactory factory;

    @Test
    public void getStrategy_ValidExpenseType_ReturnsCorrectStrategy() {
        assertEquals(activeExpenseStrategy, factory.getStrategy("active", Expense.class));
        assertEquals(inactiveExpenseStrategy, factory.getStrategy("inactive", Expense.class));
        assertEquals(allExpenseStrategy, factory.getStrategy("all", Expense.class));
    }

    @Test
    public void getStrategy_ValidSettleUpType_ReturnsCorrectStrategy() {
        assertEquals(activeSettleupStrategy, factory.getStrategy("active", SettleUp.class));
        assertEquals(inactiveSettleupStrategy, factory.getStrategy("inactive", SettleUp.class));
        assertEquals(allSettleupStrategy, factory.getStrategy("all", SettleUp.class));
    }

    @Test
    public void getStrategy_InvalidExpenseType_ThrowsCustomException() {
        CustomException exception = assertThrows(CustomException.class, () -> {
            factory.getStrategy("valid", Expense.class);
        });
        assertEquals("400", exception.getErrorCode());
        assertEquals("Invalid expense type!", exception.getErrorMessage());
    }

    @Test
    public void getStrategy_InvalidSettleUpType_ThrowsCustomException() {
        CustomException exception = assertThrows(CustomException.class, () -> {
            factory.getStrategy("invalid", SettleUp.class);
        });
        assertEquals("400", exception.getErrorCode());
        assertEquals("Invalid settle-up type!", exception.getErrorMessage());
    }

    @Test
    public void getStrategy_UnsupportedClassType_ThrowsCustomException() {
        CustomException exception = assertThrows(CustomException.class, () -> {
            factory.getStrategy("active", String.class);
        });
        assertEquals("400", exception.getErrorCode());
        assertEquals("Unsupported class type!", exception.getErrorMessage());
    }
}