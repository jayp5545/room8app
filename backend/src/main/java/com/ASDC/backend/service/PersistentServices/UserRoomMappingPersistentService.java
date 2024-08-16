package com.ASDC.backend.service.PersistentServices;

import com.ASDC.backend.entity.UserRoomMapping;
import com.ASDC.backend.exception.CustomException;
import com.ASDC.backend.repository.UserRoomRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserRoomMappingPersistentService {

    private final UserRoomRepository userRoomRepository;

    public UserRoomMappingPersistentService(UserRoomRepository userRoomRepository) {
        this.userRoomRepository = userRoomRepository;
    }

    public UserRoomMapping saveMapping(UserRoomMapping userRoomMapping){
        return userRoomRepository.save(userRoomMapping);
    }

    public Optional<UserRoomMapping> getUserRoomMappingsByUserId(long userId) {
        return userRoomRepository.findByUserID(userId);
    }

    public Optional<List<UserRoomMapping>> getAllUsersByRoom(int roomID) {
        return userRoomRepository.findAllByRoom(roomID);
    }

    public void deleteUserRoomMapping(UserRoomMapping userRoomMapping) {
        if (userRoomRepository.existsById(userRoomMapping.getId())) {
            userRoomRepository.deleteById(userRoomMapping.getId());
        } else {
            throw new CustomException("404", "User with ID " + userRoomMapping.getUserid().getId() + " doesn't exist in Room '" + userRoomMapping.getRoomid().getName() + "'");
        }
    }
}
