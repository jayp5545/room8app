package com.ASDC.backend.mapper;

import com.ASDC.backend.dto.RequestDTO.ExpenseDTORequest;
import com.ASDC.backend.dto.ResponseDTO.ExpenseDTOResponse;
import com.ASDC.backend.dto.ResponseDTO.ParticipantDTOResponse;
import com.ASDC.backend.entity.Expense;
import com.ASDC.backend.entity.Participant;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

/**
 * Mapper class for converting Expense entities to various DTO responses and vice versa.
 */
@Component
@RequiredArgsConstructor
public class ExpenseMapper {

    private static final Logger logger = LogManager.getLogger(ExpenseMapper.class);

    private final UserMapper userMapper;
    private final RoomMapper roomMapper;
    private final ParticipantMapper participantMapper;

    /**
     * Converts an ExpenseDTORequest to an Expense entity.
     *
     * @param expenseDTORequest the ExpenseDTORequest
     * @return the Expense entity
     */
    public Expense toExpense(ExpenseDTORequest expenseDTORequest) {
        if (expenseDTORequest == null) {
            logger.warn("Attempted to map null ExpenseDTORequest to Expense");
            return null;
        }

        Expense expense = new Expense();
        expense.setDescription(expenseDTORequest.getDescription());
        expense.setAmount(expenseDTORequest.getAmount());
        expense.setStatus(true);

        logger.info("Mapped ExpenseDTORequest to Expense: {}", expense);
        return expense;
    }

    /**
     * Converts an Expense entity to an ExpenseDTOResponse.
     *
     * @param expense the Expense entity
     * @return the ExpenseDTOResponse
     */
    public ExpenseDTOResponse toExpenseDTOResponse(Expense expense) {
        if (expense == null) {
            logger.warn("Attempted to map null Expense to ExpenseDTOResponse");
            return null;
        }

        ExpenseDTOResponse response = new ExpenseDTOResponse();
        response.setId(expense.getId());
        response.setDescription(expense.getDescription());
        response.setRoom(roomMapper.toRoomDTOResponse(expense.getRoom()));
        response.setPaidBy(userMapper.toUserDTOResponse(expense.getPaid_by()));
        response.setAmount(expense.getAmount());
        response.setStatus(expense.isStatus());
        response.setCreatedBy(userMapper.toUserDTOResponse(expense.getCreated_by()));
        response.setLastModifiedBy(userMapper.toUserDTOResponse(expense.getLast_modified_by()));
        response.setDateOfCreation(expense.getDate_of_creation());
        response.setLastModifiedOn(expense.getLast_modified_on());

        List<ParticipantDTOResponse> participantDTOResponses = new ArrayList<>();
        for (Participant participant : expense.getParticipants()) {
            participantDTOResponses.add(participantMapper.toParticipantDTOResponse(participant));
        }
        response.setParticipants(participantDTOResponses);

        logger.info("Mapped Expense to ExpenseDTOResponse: {}", response);
        return response;
    }
}