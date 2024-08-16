package com.ASDC.backend.service.Interface;

import com.ASDC.backend.dto.ResponseDTO.ActivityDTOResponse;
import com.ASDC.backend.dto.ResponseDTO.ActivityExpenseDTOResponse;
import com.ASDC.backend.dto.ResponseDTO.ActivitySettleUpDTOResponse;
import com.ASDC.backend.entity.Room;

import java.util.List;

public interface ActivityService {
    List<ActivityExpenseDTOResponse> getAllForExpense(Room room);
    List<ActivitySettleUpDTOResponse> getAllForSettleUp(Room Room);
    List<ActivityDTOResponse> getAll(Room Room);
}
