package com.ASDC.backend.mapper;

import com.ASDC.backend.dto.ResponseDTO.JoinRoomDTOResponse;
import com.ASDC.backend.entity.UserRoomMapping;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

/**
 * Mapper class for converting UserRoomMapping entities to JoinRoomDTOResponse.
 */
@Component
@RequiredArgsConstructor
public class UserRoomMapper {

    private static final Logger logger = LogManager.getLogger(UserRoomMapper.class);

    private final UserMapper userMapper;
    private final RoomMapper roomMapper;

    /**
     * Converts a UserRoomMapping entity to a JoinRoomDTOResponse.
     *
     * @param userRoomMapping the UserRoomMapping entity
     * @return the JoinRoomDTOResponse
     */
    public JoinRoomDTOResponse UserRoomToUserRoomDTOResponse(UserRoomMapping userRoomMapping) {
        if (userRoomMapping == null) {
            logger.warn("Attempted to map null UserRoomMapping entity to JoinRoomDTOResponse");
            return null;
        }

        JoinRoomDTOResponse joinRoomDTOResponse = new JoinRoomDTOResponse();
        joinRoomDTOResponse.setUserDTOResponse(userMapper.toUserDTOResponse(userRoomMapping.getUserid()));
        joinRoomDTOResponse.setRoomDTOResponse(roomMapper.toRoomDTOResponse(userRoomMapping.getRoomid()));
        joinRoomDTOResponse.setJoin_date(userRoomMapping.getJoin_date());

        logger.info("Mapped UserRoomMapping entity to JoinRoomDTOResponse: {}", joinRoomDTOResponse);
        return joinRoomDTOResponse;
    }
}