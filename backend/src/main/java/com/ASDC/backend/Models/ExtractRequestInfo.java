package com.ASDC.backend.Models;

import com.ASDC.backend.entity.Room;
import com.ASDC.backend.entity.User;
import com.ASDC.backend.entity.UserRoomMapping;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

/**
 * Component to extract and store request attributes for User, Room, and UserRoomMapping.
 */
@Component
@RequiredArgsConstructor
public class ExtractRequestInfo {

    private static final Logger logger = LogManager.getLogger(ExtractRequestInfo.class);

    private User user;
    private Room room;
    private UserRoomMapping userRoomMapping;

    /**
     * Sets the attributes from the HTTP request to the class fields.
     *
     * @param request the HTTP request containing the attributes
     */
    public void setAttributes(HttpServletRequest request) {
        user = (User) request.getAttribute("user");
        room = (Room) request.getAttribute("room");
        userRoomMapping = (UserRoomMapping) request.getAttribute("userRoom");

        logger.info("Extracted user, room, and userRoomMapping attributes from request.");
    }

    /**
     * Gets the user attribute.
     *
     * @return the user
     */
    public User getUser() {
        logger.debug("Getting user attribute: {}", user);
        return user;
    }

    /**
     * Gets the room attribute.
     *
     * @return the room
     */
    public Room getRoom() {
        logger.debug("Getting room attribute: {}", room);
        return room;
    }

    /**
     * Gets the userRoomMapping attribute.
     *
     * @return the userRoomMapping
     */
    public UserRoomMapping getUserRoomMapping() {
        logger.debug("Getting userRoomMapping attribute: {}", userRoomMapping);
        return userRoomMapping;
    }
}