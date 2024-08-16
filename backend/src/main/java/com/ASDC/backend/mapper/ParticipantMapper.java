package com.ASDC.backend.mapper;

import com.ASDC.backend.dto.ResponseDTO.ParticipantDTOResponse;
import com.ASDC.backend.entity.Participant;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

/**
 * Mapper class for converting Participant entities to various DTO responses.
 */
@Component
@RequiredArgsConstructor
public class ParticipantMapper {

    private static final Logger logger = LogManager.getLogger(ParticipantMapper.class);

    private final UserMapper userMapper;

    /**
     * Converts a Participant entity to a ParticipantDTOResponse.
     *
     * @param participant the Participant entity
     * @return the ParticipantDTOResponse
     */
    public ParticipantDTOResponse toParticipantDTOResponse(Participant participant) {
        if (participant == null) {
            logger.warn("Attempted to map null Participant to ParticipantDTOResponse");
            return null;
        }

        ParticipantDTOResponse participantDTOResponse = new ParticipantDTOResponse();
        participantDTOResponse.setId(participant.getId());
        participantDTOResponse.setAmount(participant.getAmount());
        participantDTOResponse.setUser(userMapper.toUserDTOResponse(participant.getUser_id()));

        logger.info("Mapped Participant to ParticipantDTOResponse: {}", participantDTOResponse);
        return participantDTOResponse;
    }
}