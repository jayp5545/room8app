package com.ASDC.backend.filter;

import com.ASDC.backend.entity.Room;
import com.ASDC.backend.entity.User;
import com.ASDC.backend.entity.UserRoomMapping;
import com.ASDC.backend.exception.CustomException;
import com.ASDC.backend.repository.RoomRepository;
import com.ASDC.backend.repository.UserRepository;
import com.ASDC.backend.repository.UserRoomRepository;
import com.ASDC.backend.Models.ExtractRequestInfo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Optional;

@Component
public class Interceptor implements HandlerInterceptor {

    private static final Logger logger = LogManager.getLogger(Interceptor.class);

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserRoomRepository userRoomRepository;

    @Autowired
    ExtractRequestInfo extractRequestInfo;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.debug("Authenticated user email: {}", email);

        User user = getUser(email);
        System.out.println(user.getEmail());
        UserRoomMapping userRoom = getUserRoomMapping(user);
        Room room = getRoom(userRoom);

        request.setAttribute("user", user);
        request.setAttribute("userRoom", userRoom);
        request.setAttribute("room", room);

        extractRequestInfo.setAttributes(request);
        return true;
    }

    public User getUser(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isEmpty()) {
            throw new CustomException("400", "User doesn't exist!");
        }

        return optionalUser.get();
    }

    public UserRoomMapping getUserRoomMapping(User user) {
        Optional<UserRoomMapping> userRoom = userRoomRepository.findByUserID(user.getId());

        if (userRoom.isEmpty()) {
            throw new CustomException("400", "User does not belong to any room!");
        }

        return userRoom.get();
    }

    public Room getRoom(UserRoomMapping userRoom) {
        Optional<Room> room = roomRepository.findById(userRoom.getRoomid().getId());

        if (room.isEmpty()) {
            throw new CustomException("400", "Room data not found!");
        }

        return room.get();
    }

}