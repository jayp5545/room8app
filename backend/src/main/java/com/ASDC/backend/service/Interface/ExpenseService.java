package com.ASDC.backend.service.Interface;

import com.ASDC.backend.dto.RequestDTO.ExpenseDTORequest;
import com.ASDC.backend.dto.ResponseDTO.ExpenseDTOResponse;
import com.ASDC.backend.entity.Room;
import com.ASDC.backend.entity.User;
import com.ASDC.backend.entity.UserRoomMapping;

import java.util.List;

public interface ExpenseService {

    ExpenseDTOResponse createExpense(ExpenseDTORequest request, User user, Room room, UserRoomMapping userRoomMapping);

    ExpenseDTOResponse modifyExpense(ExpenseDTORequest request, User user, Room room, UserRoomMapping userRoomMapping);

    void removeExpense(int id, User user, Room room);

    ExpenseDTOResponse fetchExpense(int id);

    List<ExpenseDTOResponse> listExpenses(Room room, String type);
}