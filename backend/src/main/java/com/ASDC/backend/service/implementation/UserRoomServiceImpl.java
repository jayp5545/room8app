package com.ASDC.backend.service.implementation;

import com.ASDC.backend.entity.User;
import com.ASDC.backend.entity.UserRoomMapping;
import com.ASDC.backend.exception.CustomException;
import com.ASDC.backend.repository.UserRoomRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ASDC.backend.service.Interface.UserRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserRoomServiceImpl implements UserRoomService {

    private static final Logger logger = LoggerFactory.getLogger(UserRoomServiceImpl.class);

    @Autowired
    private UserRoomRepository userRoomRepository;

    /**
     * Get all user-room mappings.
     *
     * @return List of UserRoomMapping
     */
    @Override
    public UserRoomMapping getUserRoom(User user) {
        return userRoomRepository.findByUserID(user.getId())
                .orElseThrow(() -> new CustomException("400", "User Room data with userID " + user.getId() + " does not exist."));
    }
}
