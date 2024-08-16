package com.ASDC.backend.mapper;

import com.ASDC.backend.dto.RequestDTO.RoomDTORequest;
import com.ASDC.backend.dto.ResponseDTO.RoomDTOResponse;
import com.ASDC.backend.entity.Room;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

/**
 * Mapper class for converting Room entities to various DTO requests and responses.
 */
@Component
@RequiredArgsConstructor
public class RoomMapper {

    private static final Logger logger = LogManager.getLogger(RoomMapper.class);

    /**
     * Converts a RoomDTORequest to a Room entity.
     *
     * @param roomDTO the RoomDTORequest
     * @return the Room entity
     */
    public Room toRoom(RoomDTORequest roomDTO) {
        if (roomDTO == null) {
            logger.warn("Attempted to map null RoomDTORequest to Room entity");
            return null;
        }

        Room room = new Room();
        room.setName(roomDTO.getName());

        logger.info("Mapped RoomDTORequest to Room entity: {}", room);
        return room;
    }

    /**
     * Converts a Room entity to a RoomDTOResponse.
     *
     * @param room the Room entity
     * @return the RoomDTOResponse
     */
    public RoomDTOResponse toRoomDTOResponse(Room room) {
        if (room == null) {
            logger.warn("Attempted to map null Room entity to RoomDTOResponse");
            return null;
        }

        RoomDTOResponse roomDTOResponse = new RoomDTOResponse();
        roomDTOResponse.setId(room.getId());
        roomDTOResponse.setName(room.getName());
        roomDTOResponse.setCode(room.getCode());
        roomDTOResponse.setMembers(room.getMembers());
        roomDTOResponse.setActive(room.isActive());

        logger.info("Mapped Room entity to RoomDTOResponse: {}", roomDTOResponse);
        return roomDTOResponse;
    }
}