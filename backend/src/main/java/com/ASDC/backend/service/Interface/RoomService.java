package com.ASDC.backend.service.Interface;

import com.ASDC.backend.dto.RequestDTO.JoinRoomDTORequest;
import com.ASDC.backend.dto.RequestDTO.RoomDTORequest;
import com.ASDC.backend.dto.ResponseDTO.JoinRoomDTOResponse;
import com.ASDC.backend.dto.ResponseDTO.RoomDTOResponse;
import com.ASDC.backend.dto.ResponseDTO.UserDTOResponse;
import com.ASDC.backend.entity.Room;
import com.ASDC.backend.entity.User;
import com.ASDC.backend.entity.UserRoomMapping;

import java.util.List;

public interface RoomService {
    RoomDTOResponse createRoom(RoomDTORequest roomDTORequest);

    RoomDTOResponse getRoomById(int id);

    RoomDTOResponse updateRoom(RoomDTORequest roomDTORequest);

    void deleteRoom(int id);

    JoinRoomDTOResponse joinRoom(JoinRoomDTORequest joinRoomDTORequest);

    void leaveRoom(User user, Room room, UserRoomMapping userRoomMapping);

    List<UserDTOResponse> getAllUsersByRoom(int roomID);
}
