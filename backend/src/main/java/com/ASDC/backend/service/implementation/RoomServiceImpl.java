package com.ASDC.backend.service.implementation;

import com.ASDC.backend.dto.RequestDTO.JoinRoomDTORequest;
import com.ASDC.backend.dto.RequestDTO.RoomDTORequest;
import com.ASDC.backend.dto.ResponseDTO.JoinRoomDTOResponse;
import com.ASDC.backend.dto.ResponseDTO.RoomDTOResponse;
import com.ASDC.backend.dto.ResponseDTO.UserDTOResponse;
import com.ASDC.backend.entity.Room;
import com.ASDC.backend.entity.User;
import com.ASDC.backend.entity.UserRoomMapping;
import com.ASDC.backend.exception.CustomException;
import com.ASDC.backend.mapper.RoomMapper;
import com.ASDC.backend.mapper.UserMapper;
import com.ASDC.backend.mapper.UserRoomMapper;
import com.ASDC.backend.repository.UserRepository;
import com.ASDC.backend.service.Interface.RoomService;
import com.ASDC.backend.service.PersistentServices.RoomPersistentService;
import com.ASDC.backend.service.PersistentServices.UserRoomMappingPersistentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private static final Logger logger = LogManager.getLogger(RoomServiceImpl.class);

    private final RoomPersistentService roomPersistentService;
    private final UserRoomMappingPersistentService userRoomMappingPersistentService;
    private final RoomMapper roomMapper;
    private final UserRoomMapper userRoomMapper;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    /**
     * Creates a new room.
     *
     * @param roomDTORequest the room request DTO
     * @return the room response DTO
     */
    @Override
    public RoomDTOResponse createRoom(RoomDTORequest roomDTORequest) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new CustomException("400", "User doesn't exist");
        }

        if (getRoomByName(roomDTORequest.getName()) != null) {
            throw new CustomException("400", "Room already exists with the given name!");
        }

        if (getUserRoomMapping(user.get()) != null) {
            throw new CustomException("400", "User is already in a room!");
        }

        Room room = createRoomObject(roomDTORequest);
        int code = generateUniqueRoomCode();
        room.setCode(code);

        Room response = roomPersistentService.saveRoom(room);

        JoinRoomDTORequest joinRoomDTORequest = new JoinRoomDTORequest();
        joinRoomDTORequest.setJoinCode(code);

        joinRoomTODB(joinRoomDTORequest, user.get(), response);

        logger.info("Room created successfully: {}", room.getName());

        return roomMapper.toRoomDTOResponse(response);
    }

    private Room createRoomObject(RoomDTORequest roomDTORequest) {
        Room room = new Room();
        room.setName(roomDTORequest.getName());
        room.setActive(true);
        room.setMembers(0);
        room.setDate_of_creation(LocalDateTime.now());
        room.setCode(generateUniqueRoomCode());
        return room;
    }

    private int generateUniqueRoomCode() {
        int code;
        Random random = new Random();
        do {
            code = 10000 + random.nextInt(89999);
        } while (roomPersistentService.getRoomByCode(code).isPresent());
        return code;
    }

    /**
     * Retrieves a room by its ID.
     *
     * @param id the room ID
     * @return the room response DTO
     */
    @Override
    public RoomDTOResponse getRoomById(int id) {
        Optional<Room> room = roomPersistentService.getRoomById(id);

        if (room.isEmpty()) {
            throw new CustomException("400", "Room doesn't exist!");
        }

        logger.info("Room retrieved successfully: {}", room.get().getName());

        return roomMapper.toRoomDTOResponse(room.get());
    }

    /**
     * Updates a room.
     *
     * @param roomDTORequest the room request DTO
     * @return the room response DTO
     */
    @Override
    public RoomDTOResponse updateRoom(RoomDTORequest roomDTORequest) {
        Optional<Room> room = roomPersistentService.getRoomById(roomDTORequest.getId());

        if (room.isEmpty()) {
            throw new CustomException("400", "Room doesn't exist!");
        }

        Room updatedRoom = room.get();
        updatedRoom.setName(roomDTORequest.getName());
        Room response = roomPersistentService.saveRoom(updatedRoom);

        logger.info("Room updated successfully: {}", updatedRoom.getName());

        return roomMapper.toRoomDTOResponse(response);
    }

    /**
     * Deletes a room by its ID.
     *
     * @param id the room ID
     */
    @Override
    public void deleteRoom(int id) {
        RoomDTOResponse room = getRoomById(id);
        roomPersistentService.deleteRoomById(id);
        logger.info("Room deleted successfully with ID {}", id);
    }

    /**
     * Joins a room.
     *
     * @param joinRoomDTORequest the join room request DTO
     * @return the join room response DTO
     */
    @Override
    public JoinRoomDTOResponse joinRoom(JoinRoomDTORequest joinRoomDTORequest) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new CustomException("400", "User doesn't exist");
        }

        Room room = getRoomByCode(joinRoomDTORequest.getJoinCode());

        if (room == null) {
            throw new CustomException("400", "Incorrect join room code!");
        }

        Optional<UserRoomMapping> userRoomMapping = userRoomMappingPersistentService.getUserRoomMappingsByUserId(user.get().getId());
        if (userRoomMapping.isPresent()) {
            throw new CustomException("400", "User already in room!");
        }

        logger.info("User {} joined room {}", user.get().getEmail(), room.getName());

        return joinRoomTODB(joinRoomDTORequest, user.get(), room);
    }

    private JoinRoomDTOResponse joinRoomTODB(JoinRoomDTORequest joinRoomDTORequest, User user, Room room) {
        room.setMembers(room.getMembers() + 1);
        roomPersistentService.saveRoom(room);

        UserRoomMapping userRoomMapping = createUserRoomMapping(user, room);
        UserRoomMapping response = userRoomMappingPersistentService.saveMapping(userRoomMapping);

        return userRoomMapper.UserRoomToUserRoomDTOResponse(response);
    }

    private UserRoomMapping createUserRoomMapping(User user, Room room) {
        UserRoomMapping userRoomMapping = new UserRoomMapping();
        userRoomMapping.setUserid(user);
        userRoomMapping.setRoomid(room);
        userRoomMapping.setJoin_date(LocalDateTime.now());
        userRoomMapping.setRemove_date(null);
        return userRoomMapping;
    }

    /**
     * Leaves a room.
     *
     * @param user the user
     * @param room the room
     * @param userRoomMapping the user-room mapping
     */
    @Transactional
    @Override
    public void leaveRoom(User user, Room room, UserRoomMapping userRoomMapping) {
        userRoomMappingPersistentService.deleteUserRoomMapping(userRoomMapping);
        room.setMembers(room.getMembers() - 1);
        roomPersistentService.saveRoom(room);
        logger.info("User {} left room {}", user.getEmail(), room.getName());
    }

    private Room getRoomByName(String roomName) {
        Optional<Room> room = roomPersistentService.getRoomByName(roomName);

        return room.orElse(null);
    }

    private Room getRoomByCode(int code) {
        Optional<Room> room = roomPersistentService.getRoomByCode(code);

        return room.orElse(null);
    }

    public UserRoomMapping getUserRoomMapping(User user) {
        Optional<UserRoomMapping> userRoom = userRoomMappingPersistentService.getUserRoomMappingsByUserId(user.getId());

        return userRoom.orElse(null);
    }

    /**
     * Retrieves all users by room ID.
     *
     * @param roomID the room ID
     * @return the list of user response DTOs
     */
    @Override
    public List<UserDTOResponse> getAllUsersByRoom(int roomID) {
        Optional<List<UserRoomMapping>> userRoomMappingList = userRoomMappingPersistentService.getAllUsersByRoom(roomID);

        if (userRoomMappingList.isEmpty()) {
            return new ArrayList<>();
        }

        List<UserDTOResponse> response = new ArrayList<>();
        for (UserRoomMapping currMapping : userRoomMappingList.get()) {
            response.add(userMapper.toUserDTOResponse(currMapping.getUserid()));
        }

        logger.info("Retrieved {} users for room ID {}", response.size(), roomID);

        return response;
    }
}