package com.ASDC.backend.service.implementation;

import com.ASDC.backend.dto.RequestDTO.ProfileDTORequest;
import com.ASDC.backend.dto.ResponseDTO.ProfileDTOResponse;
import com.ASDC.backend.dto.ResponseDTO.UserDTOResponse;
import com.ASDC.backend.entity.Room;
import com.ASDC.backend.entity.User;
import com.ASDC.backend.entity.UserRoomMapping;
import com.ASDC.backend.exception.CustomException;
import com.ASDC.backend.mapper.RoomMapper;
import com.ASDC.backend.mapper.UserMapper;
import com.ASDC.backend.service.Interface.ProfileService;
import com.ASDC.backend.service.PersistentServices.RoomPersistentService;
import com.ASDC.backend.service.PersistentServices.UserRoomMappingPersistentService;
import com.ASDC.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service implementation for managing user profiles.
 */
@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private static final Logger logger = LogManager.getLogger(ProfileServiceImpl.class);

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoomMapper roomMapper;
    private final RoomPersistentService roomPersistentService;
    private final UserRoomMappingPersistentService userRoomMappingPersistentService;

    /**
     * Change the name of a user.
     *
     * @param email               the email of the user.
     * @param profileDTORequest   the new profile details.
     * @return the updated user profile response.
     */
    @Override
    public UserDTOResponse changeName(String email, ProfileDTORequest profileDTORequest) {
        logger.info("Changing name for user with email: {}", email);
        User user = verifyUser(email);

        user.setFirstName(profileDTORequest.getFirstName());
        user.setLastName(profileDTORequest.getLastName());

        User renamedUser = userRepository.save(user);
        logger.info("Name changed successfully for user with email: {}", email);

        return userMapper.toUserDTOResponse(renamedUser);
    }

    /**
     * Get the profile of a user.
     *
     * @param email the email of the user.
     * @return the profile response.
     */
    @Override
    public ProfileDTOResponse getProfile(String email) {
        logger.info("Fetching profile for user with email: {}", email);
        User user = verifyUser(email);

        ProfileDTOResponse profileDTOResponse = new ProfileDTOResponse();
        profileDTOResponse.setUserDTOResponse(userMapper.toUserDTOResponse(user));

        Optional<UserRoomMapping> userRoomMapping = userRoomMappingPersistentService.getUserRoomMappingsByUserId(user.getId());
        if (userRoomMapping.isEmpty()) {
            logger.warn("No room mapping found for user with email: {}", email);
            profileDTOResponse.setRoomDTOResponse(null);
            profileDTOResponse.setJoinDate(null);
            return profileDTOResponse;
        }

        Optional<Room> room = roomPersistentService.getRoomById(userRoomMapping.get().getRoomid().getId());
        if (room.isPresent()) {
            profileDTOResponse.setRoomDTOResponse(roomMapper.toRoomDTOResponse(room.get()));
            profileDTOResponse.setJoinDate(userRoomMapping.get().getJoin_date());
            logger.info("Room details added to profile for user with email: {}", email);
        } else {
            logger.warn("Room not found for user with email: {}", email);
        }

        return profileDTOResponse;
    }

    /**
     * Verify the existence of a user by email.
     *
     * @param email the email of the user.
     * @return the user entity.
     */
    private User verifyUser(String email) {
        logger.info("Verifying user with email: {}", email);
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            logger.error("User with email {} doesn't exist!", email);
            throw new CustomException("400", "User doesn't exist!");
        }
        return userOptional.get();
    }
}