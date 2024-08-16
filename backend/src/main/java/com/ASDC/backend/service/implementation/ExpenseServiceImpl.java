package com.ASDC.backend.service.implementation;

import com.ASDC.backend.dto.RequestDTO.ExpenseDTORequest;
import com.ASDC.backend.dto.ResponseDTO.ExpenseDTOResponse;
import com.ASDC.backend.entity.*;
import com.ASDC.backend.exception.CustomException;
import com.ASDC.backend.mapper.ExpenseMapper;
import com.ASDC.backend.repository.ActivityRepository;
import com.ASDC.backend.service.Interface.ExpenseService;
import com.ASDC.backend.service.Interface.RetrievalStrategy;
import com.ASDC.backend.service.Interface.UserService;
import com.ASDC.backend.service.PersistentServices.ExpensePersistentService;
import com.ASDC.backend.service.PersistentServices.ParticipantPersistentService;
import com.ASDC.backend.service.StrategyFactory.ExpenseRetrievalStrategyFactory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Service implementation for handling expenses.
 */
@Service
@RequiredArgsConstructor
public class ExpenseServiceImpl implements ExpenseService {

    private static final Logger logger = LogManager.getLogger(ExpenseServiceImpl.class);

    private final ExpenseMapper expenseMapper;
    private final ExpensePersistentService expensePersistentService;
    private final ParticipantPersistentService participantPersistentService;
    private final ActivityLoggingServiceImpl activityLoggingServiceImpl;
    private final UserService userService;
    private final ExpenseRetrievalStrategyFactory strategyFactory;
    private final ActivityRepository activityRepository;

    /**
     * Creates a new expense.
     *
     * @param request the expense request DTO
     * @param user the user creating the expense
     * @param room the room where the expense is created
     * @param userRoomMapping the user-room mapping
     * @return the response DTO with expense details
     */
    @Override
    public ExpenseDTOResponse createExpense(ExpenseDTORequest request, User user, Room room, UserRoomMapping userRoomMapping) {
        logger.info("Creating expense: {}", request.getDescription());

        User paidBy = validateUser(request.getPaidBy());
        List<User> participants = validateParticipants(request.getParticipantEmails());

        ExpenseDTOResponse response = saveExpense(user, room, request.getDescription(), request.getAmount(), paidBy, participants, Activity.ActivityType.Expense_ADD);

        logger.info("Expense created successfully with ID: {}", response.getId());
        return response;
    }

    /**
     * Modifies an existing expense.
     *
     * @param request the expense request DTO
     * @param user the user modifying the expense
     * @param room the room where the expense is modified
     * @param userRoomMapping the user-room mapping
     * @return the response DTO with expense details
     */
    @Override
    public ExpenseDTOResponse modifyExpense(ExpenseDTORequest request, User user, Room room, UserRoomMapping userRoomMapping) {
        logger.info("Modifying expense ID: {}", request.getId());

        User paidBy = validateUser(request.getPaidBy());
        List<User> participants = validateParticipants(request.getParticipantEmails());
        Expense expense = expensePersistentService.getExpense(request.getId());

        if (!expense.isStatus()) {
            logger.error("Expense validation failed for expense edit: {}", request);
            throw new CustomException("400", "Deleted expense can't be modified!");
        }

        ExpenseDTOResponse response = updateExpenseDetails(user, room, request.getDescription(), request.getAmount(), paidBy, participants, expense, Activity.ActivityType.Expense_EDIT);

        logger.info("Expense modified successfully with ID: {}", response.getId());
        return response;
    }

    /**
     * Removes an existing expense.
     *
     * @param id the ID of the expense to be removed
     * @param user the user removing the expense
     * @param room the room where the expense is removed
     */
    @Override
    public void removeExpense(int id, User user, Room room) {
        logger.info("Removing expense ID: {}", id);

        Expense expense = expensePersistentService.getExpense(id);
        markExpenseAsDeleted(user, room, expense, Activity.ActivityType.Expense_DELETE);

        logger.info("Expense removed successfully with ID: {}", id);
    }

    /**
     * Fetches the details of a specific expense.
     *
     * @param id the ID of the expense to be fetched
     * @return the response DTO with expense details
     */
    @Override
    public ExpenseDTOResponse fetchExpense(int id) {
        logger.info("Fetching expense ID: {}", id);

        Expense expense = getExpenseById(id);
        ExpenseDTOResponse response = expenseMapper.toExpenseDTOResponse(expense);

        logger.info("Expense fetched successfully with ID: {}", id);
        return response;
    }

    /**
     * Lists all expenses in a specific room and of a specific type.
     *
     * @param room the room where expenses are listed
     * @param type the type of expenses to list
     * @return a list of response DTOs with expense details
     */
    @Override
    public List<ExpenseDTOResponse> listExpenses(Room room, String type) {
        logger.info("Listing expenses for room ID: {} with type: {}", room.getId(), type);

        List<Expense> expenses = findExpensesByType(room, type);
        List<ExpenseDTOResponse> responses = new ArrayList<>();
        for (Expense expense : expenses) {
            responses.add(expenseMapper.toExpenseDTOResponse(expense));
        }

        logger.info("Listed {} expenses for room ID: {}", responses.size(), room.getId());
        return responses;
    }

    /**
     * Validates a user by their email.
     *
     * @param email the email of the user to validate
     * @return the validated user
     */
    private User validateUser(String email) {
        logger.debug("Validating user with email: {}", email);
        try {
            return userService.getUserByEmail(email);
        } catch (CustomException customException) {
            logger.error("User validation failed for email: {}", email, customException);
            throw new CustomException("400", "User with email " + email + " does not exist.");
        }
    }

    /**
     * Validates a list of participants by their emails.
     *
     * @param emails the emails of the participants to validate
     * @return a list of validated participants
     */
    private List<User> validateParticipants(List<String> emails) {
        logger.debug("Validating participants: {}", emails);
        List<User> participants = new ArrayList<>();
        for (String email : emails) {
            participants.add(validateUser(email));
        }
        return participants;
    }

    /**
     * Retrieves an expense by its ID.
     *
     * @param id the ID of the expense to retrieve
     * @return the retrieved expense
     */
    private Expense getExpenseById(int id) {
        logger.debug("Retrieving expense by ID: {}", id);
        try {
            return expensePersistentService.getExpense(id);
        } catch (CustomException e) {
            logger.error("Expense retrieval failed for ID: {}: {}", id, e.getMessage());
            throw new CustomException("404", "Expense not found.");
        }
    }

    /**
     * Finds expenses by their type in a specific room.
     *
     * @param room the room where expenses are to be found
     * @param type the type of expenses to find
     * @return a list of found expenses
     */
    private List<Expense> findExpensesByType(Room room, String type) {
        logger.debug("Finding expenses by type: {} for room ID: {}", type, room.getId());
        RetrievalStrategy<Expense> strategy = strategyFactory.getStrategy(type, Expense.class);
        return strategy.retrieveItems(room);
    }

    /**
     * Saves a new expense.
     *
     * @param user the user creating the expense
     * @param room the room where the expense is created
     * @param description the description of the expense
     * @param amount the amount of the expense
     * @param paidBy the user who paid for the expense
     * @param participants the participants of the expense
     * @param activityType the type of activity to log
     * @return the response DTO with expense details
     */
    @Transactional
    private ExpenseDTOResponse saveExpense(User user, Room room, String description, double amount, User paidBy, List<User> participants, Activity.ActivityType activityType) {
        logger.debug("Saving new expense: {} for room ID: {}", description, room.getId());

        double amountPerParticipant = amount / (participants.size() + 1);

        Expense expense = Expense.builder()
                .description(description)
                .amount(amount)
                .paid_by(paidBy)
                .date_of_creation(LocalDateTime.now())
                .last_modified_on(LocalDateTime.now())
                .created_by(user)
                .last_modified_by(user)
                .room(room)
                .status(true)
                .build();
        Expense savedExpense = expensePersistentService.saveExpense(expense);

        logger.debug("Expense saved with ID: {}", savedExpense.getId());

        List<Participant> participantEntities = createParticipants(participants, amountPerParticipant, savedExpense);
        List<Participant> savedParticipants = participantPersistentService.saveParticipants(participantEntities);

        logger.debug("Participants saved for expense ID: {}", savedExpense.getId());

        savedExpense.setParticipants(savedParticipants);

        activityLoggingServiceImpl.logExpenseActivity(user, room, savedExpense, activityType, user.getFirstName() + " added '" + description + "'");
        logger.debug("Activity logged for expense ID: {}", savedExpense.getId());

        return expenseMapper.toExpenseDTOResponse(savedExpense);
    }

    /**
     * Updates the details of an existing expense.
     *
     * @param user the user modifying the expense
     * @param room the room where the expense is modified
     * @param description the new description of the expense
     * @param amount the new amount of the expense
     * @param paidBy the new user who paid for the expense
     * @param participants the new participants of the expense
     * @param expense the expense to update
     * @param activityType the type of activity to log
     * @return the response DTO with updated expense details
     */
    @Transactional
    private ExpenseDTOResponse updateExpenseDetails(User user, Room room, String description, double amount, User paidBy, List<User> participants, Expense expense, Activity.ActivityType activityType) {
        logger.debug("Updating expense ID: {}", expense.getId());

        participantPersistentService.deleteParticipants(expense.getParticipants());

        expense.getParticipants().clear();

        expense.setDescription(description);
        expense.setAmount(amount);
        expense.setPaid_by(paidBy);
        expense.setLast_modified_on(LocalDateTime.now());
        expense.setLast_modified_by(user);

        Expense updatedExpense = expensePersistentService.saveExpense(expense);

        List<Participant> participantList = createParticipants(participants, amount / (participants.size() + 1), updatedExpense);

        List<Participant> savedParticipants = participantPersistentService.saveParticipants(participantList);

        updatedExpense.setParticipants(savedParticipants);

        logger.debug("Expense updated with ID: {}", updatedExpense.getId());

        /*activityLoggingServiceImpl.logExpenseActivity(user, room, updatedExpense, activityType, user.getFirstName() + " modified '" + description + "'");
        logger.debug("Activity logged for expense ID: {}", updatedExpense.getId());*/

        return expenseMapper.toExpenseDTOResponse(updatedExpense);
    }

    /**
     * Marks an expense as deleted.
     *
     * @param user the user marking the expense as deleted
     * @param room the room where the expense is marked as deleted
     * @param expense the expense to mark as deleted
     * @param activityType the type of activity to log
     */
    @Transactional
    private void markExpenseAsDeleted(User user, Room room, Expense expense, Activity.ActivityType activityType) {
        logger.debug("Marking expense ID: {} as deleted", expense.getId());

        expense.setStatus(false);
        expense.setLast_modified_on(LocalDateTime.now());
        expense.setLast_modified_by(user);
        expensePersistentService.saveExpense(expense);

        activityLoggingServiceImpl.logExpenseActivity(user, room, expense, activityType, user.getFirstName() + " removed expense '" + expense.getDescription() + "'");
        logger.debug("Activity logged for deleted expense ID: {}", expense.getId());
    }

    /**
     * Creates participant entities for an expense.
     *
     * @param users the users participating in the expense
     * @param amountPerParticipant the amount each participant owes
     * @param expense the expense they are participating in
     * @return a list of participant entities
     */
    private List<Participant> createParticipants(List<User> users, double amountPerParticipant, Expense expense) {
        logger.debug("Creating participants for expense ID: {}", expense.getId());

        List<Participant> participants = new ArrayList<>();
        for (User user : users) {
            participants.add(Participant.builder()
                    .user_id(user)
                    .expense(expense)
                    .amount(amountPerParticipant)
                    .build());
        }

        logger.debug("Created {} participants for expense ID: {}", participants.size(), expense.getId());
        return participants;
    }
}