package com.ASDC.backend.mapper;

import com.ASDC.backend.dto.RequestDTO.SettleUpDTORequest;
import com.ASDC.backend.dto.ResponseDTO.SettleUpDTOResponse;
import com.ASDC.backend.entity.SettleUp;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

/**
 * Mapper class for converting SettleUp entities to various DTO requests and responses.
 */
@Component
@RequiredArgsConstructor
public class SettleUpMapper {

    private static final Logger logger = LogManager.getLogger(SettleUpMapper.class);

    private final UserMapper userMapper;
    private final RoomMapper roomMapper;

    /**
     * Converts a SettleUp entity to a SettleUpDTOResponse.
     *
     * @param settleUp the SettleUp entity
     * @return the SettleUpDTOResponse
     */
    public SettleUpDTOResponse toSettleUpDTOResponse(SettleUp settleUp) {
        if (settleUp == null) {
            logger.warn("Attempted to map null SettleUp entity to SettleUpDTOResponse");
            return null;
        }

        SettleUpDTOResponse settleUpDTOResponse = new SettleUpDTOResponse();
        settleUpDTOResponse.setId(settleUp.getId());
        settleUpDTOResponse.setStatus(settleUp.isStatus());
        settleUpDTOResponse.setRoom(roomMapper.toRoomDTOResponse(settleUp.getRoom()));
        settleUpDTOResponse.setPaidBy(userMapper.toUserDTOResponse(settleUp.getPaid_by()));
        settleUpDTOResponse.setPaidTo(userMapper.toUserDTOResponse(settleUp.getPaid_to()));
        settleUpDTOResponse.setAmount(settleUp.getAmount());
        settleUpDTOResponse.setCreatedBy(userMapper.toUserDTOResponse(settleUp.getCreated_by()));
        settleUpDTOResponse.setLastModifiedBy(userMapper.toUserDTOResponse(settleUp.getLast_modified_by()));
        settleUpDTOResponse.setDateOfCreation(settleUp.getDate_of_creation());
        settleUpDTOResponse.setLastModifiedOn(settleUp.getLast_modified_on());

        logger.info("Mapped SettleUp entity to SettleUpDTOResponse: {}", settleUpDTOResponse);
        return settleUpDTOResponse;
    }

    /**
     * Converts a SettleUpDTORequest to a SettleUp entity.
     *
     * @param settleUpDTORequest the SettleUpDTORequest
     * @return the SettleUp entity
     */
    public SettleUp toSettleUp(SettleUpDTORequest settleUpDTORequest) {
        if (settleUpDTORequest == null) {
            logger.warn("Attempted to map null SettleUpDTORequest to SettleUp entity");
            return null;
        }

        SettleUp settleUp = new SettleUp();
        settleUp.setAmount(settleUpDTORequest.getAmount());

        logger.info("Mapped SettleUpDTORequest to SettleUp entity: {}", settleUp);
        return settleUp;
    }
}