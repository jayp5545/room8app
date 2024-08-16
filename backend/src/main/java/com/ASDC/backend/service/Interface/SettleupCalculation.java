package com.ASDC.backend.service.Interface;

import com.ASDC.backend.Models.SettleUpAmountDTOProjection;
import com.ASDC.backend.dto.ResponseDTO.ExpenseDTOResponse;
import com.ASDC.backend.dto.ResponseDTO.SettleUpAmountDTOResponse;
import com.ASDC.backend.entity.Room;
import com.ASDC.backend.entity.User;

import java.util.List;

public interface SettleupCalculation {
    List<SettleUpAmountDTOResponse> calculate(Room room, User user, List<ExpenseDTOResponse> expenseDTOResponseList, List<SettleUpAmountDTOProjection> settleUpList);
}
