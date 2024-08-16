package com.ASDC.backend.service.Interface;


import com.ASDC.backend.dto.RequestDTO.SettleUpDTORequest;
import com.ASDC.backend.dto.ResponseDTO.SettleUpAmountDTOResponse;
import com.ASDC.backend.dto.ResponseDTO.SettleUpDTOResponse;
import com.ASDC.backend.entity.Room;
import com.ASDC.backend.entity.User;
import com.ASDC.backend.entity.UserRoomMapping;

import java.util.List;

public interface SettleUpService {

    List<SettleUpAmountDTOResponse> calculateSettleUps(Room room, User user);

    SettleUpDTOResponse createSettleUp(SettleUpDTORequest settleUpDTORequest, User user, Room room, UserRoomMapping userRoom);

    SettleUpDTOResponse updateSettleUp(SettleUpDTORequest settleUpDTORequest, User user, Room room, UserRoomMapping userRoom);

    SettleUpDTOResponse findSettleUpById(int settleUpID, User user, Room room);

    void deleteSettleUp(int settleUpID, User user, Room room);

    List<SettleUpDTOResponse> findAllSettleUps(Room room, User user, String type);
}
