package com.ASDC.backend.controller;

import com.ASDC.backend.Models.ExtractRequestInfo;
import com.ASDC.backend.dto.RequestDTO.ExpenseDTORequest;
import com.ASDC.backend.dto.ResponseDTO.ExpenseDTOResponse;
import com.ASDC.backend.entity.Room;
import com.ASDC.backend.entity.User;
import com.ASDC.backend.entity.UserRoomMapping;
import com.ASDC.backend.exception.CustomException;
import com.ASDC.backend.service.Interface.ExpenseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Controller for handling expense-related API endpoints.
 */
@RestController
@RequestMapping("/api/v1/expense")
@RequiredArgsConstructor
public class ExpenseController {

    private static final Logger logger = LogManager.getLogger(ExpenseController.class);

    private final ExpenseService expenseService;
    private final ExtractRequestInfo extractRequestInfo;

    /**
     * Add a new expense.
     *
     * @param expenseDTORequest the expense request DTO
     * @return the created expense DTO response
     */
    @PostMapping("/add")
    public ResponseEntity<ExpenseDTOResponse> addExpense(@Valid @RequestBody ExpenseDTORequest expenseDTORequest) {
        User user = extractRequestInfo.getUser();
        Room room = extractRequestInfo.getRoom();
        UserRoomMapping userRoom = extractRequestInfo.getUserRoomMapping();

        logger.info("Creating expense for user: {} {}", user.getFirstName(), user.getLastName());

        ExpenseDTOResponse response = expenseService.createExpense(expenseDTORequest, user, room, userRoom);

        logger.info("Created expense with ID: {}", response.getId());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Update an existing expense.
     *
     * @param expenseDTORequest the expense request DTO
     * @return the updated expense DTO response
     */
    @PutMapping("/update")
    public ResponseEntity<ExpenseDTOResponse> updateExpense(@Valid @RequestBody ExpenseDTORequest expenseDTORequest) {
        User user = extractRequestInfo.getUser();
        Room room = extractRequestInfo.getRoom();
        UserRoomMapping userRoom = extractRequestInfo.getUserRoomMapping();

        logger.info("Updating expense for user: {} {}", user.getFirstName(), user.getLastName());

        ExpenseDTOResponse response = expenseService.modifyExpense(expenseDTORequest, user, room, userRoom);

        logger.info("Updated expense with ID: {}", response.getId());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Delete an expense by its ID.
     *
     * @param expenseID the ID of the expense to delete
     * @return a response entity with success message
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteExpense(@PathVariable(name = "id") int expenseID) {
        validateExpenseId(expenseID);

        User user = extractRequestInfo.getUser();
        Room room = extractRequestInfo.getRoom();

        logger.info("Deleting expense with ID: {}", expenseID);

        expenseService.removeExpense(expenseID, user, room);

        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    /**
     * Get an expense by its ID.
     *
     * @param expenseID the ID of the expense
     * @return the expense DTO response
     */
    @GetMapping("/get/{id}")
    public ResponseEntity<ExpenseDTOResponse> getExpense(@PathVariable(name = "id") int expenseID) {
        validateExpenseId(expenseID);

        logger.info("Fetching expense with ID: {}", expenseID);

        ExpenseDTOResponse response = expenseService.fetchExpense(expenseID);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Get all expenses by type. (Type - all, active, inactive)
     *
     * @param type the type of expenses to fetch
     * @return a list of expense DTO responses
     */
    @GetMapping("/get/all/{type}")
    public ResponseEntity<List<ExpenseDTOResponse>> getAllExpense(@PathVariable(name = "type") String type) {
        Room room = extractRequestInfo.getRoom();

        logger.info("Fetching all expenses of type: {}", type);

        List<ExpenseDTOResponse> response = expenseService.listExpenses(room, type);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Validate the expense ID.
     *
     * @param expenseID the expense ID to validate
     */
    private void validateExpenseId(int expenseID) {
        if (expenseID <= 0) {
            logger.error("Invalid expense ID: {}", expenseID);
            throw new CustomException("400", "Expense ID must be greater than zero.");
        }
    }
}