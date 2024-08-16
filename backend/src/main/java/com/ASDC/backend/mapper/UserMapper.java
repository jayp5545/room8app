package com.ASDC.backend.mapper;

import com.ASDC.backend.dto.RequestDTO.UserDTORequest;
import com.ASDC.backend.dto.ResponseDTO.UserDTOResponse;
import com.ASDC.backend.entity.User;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

/**
 * Mapper class for converting User entities to various DTO requests and responses.
 */
@Component
@RequiredArgsConstructor
public class UserMapper {

    private static final Logger logger = LogManager.getLogger(UserMapper.class);

    /**
     * Converts a UserDTORequest to a User entity.
     *
     * @param userDTORequest the UserDTORequest
     * @return the User entity
     */
    public User toUser(UserDTORequest userDTORequest) {
        if (userDTORequest == null) {
            logger.warn("Attempted to map null UserDTORequest to User entity");
            return null;
        }

        User user = new User();
        user.setFirstName(userDTORequest.getFirstName());
        user.setLastName(userDTORequest.getLastName());
        user.setEmail(userDTORequest.getEmail());
        user.setPassword(userDTORequest.getPassword());

        logger.info("Mapped UserDTORequest to User entity: {}", user);
        return user;
    }

    /**
     * Converts a User entity to a UserDTOResponse.
     *
     * @param user the User entity
     * @return the UserDTOResponse
     */
    public UserDTOResponse toUserDTOResponse(User user) {
        if (user == null) {
            logger.warn("Attempted to map null User entity to UserDTOResponse");
            return null;
        }

        UserDTOResponse userDTOResponse = new UserDTOResponse();
        userDTOResponse.setId(user.getId());
        userDTOResponse.setFirstName(user.getFirstName());
        userDTOResponse.setLastName(user.getLastName());
        userDTOResponse.setEmail(user.getEmail());

        logger.info("Mapped User entity to UserDTOResponse: {}", userDTOResponse);
        return userDTOResponse;
    }
}