package com.ASDC.backend.service.PersistentServices;

import com.ASDC.backend.entity.Expense;
import com.ASDC.backend.entity.Room;
import com.ASDC.backend.exception.CustomException;
import com.ASDC.backend.repository.ExpenseRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ExpensePersistentService {

    private final ExpenseRepository expenseRepository;

    @Autowired
    public ExpensePersistentService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    public Expense saveExpense(Expense expense) {
        return expenseRepository.save(expense);
    }

    public Expense getExpense(int id){
        return expenseRepository.findActiveById(id)
                .orElseThrow(() -> new CustomException("400", "Expense with ID " + id + " does not exist."));
    }

    public List<Expense> getAllExpense(Room room){
        return expenseRepository.findAllByRoom(room);
    }

    public List<Expense> getAllExpenseActive(int roomID){
        return expenseRepository.findAllByRoomActive(roomID);
    }

    public List<Expense> getAllExpenseInActive(int roomID){
        return expenseRepository.findAllByRoomInActive(roomID);
    }
}
