package com.ASDC.backend.service;

import com.ASDC.backend.DummyData.DummyData;
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
import com.ASDC.backend.service.implementation.RoomServiceImpl;
import com.ASDC.backend.service.PersistentServices.RoomPersistentService;
import com.ASDC.backend.service.PersistentServices.UserRoomMappingPersistentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RoomServiceImplTest {

    @Mock
    private RoomPersistentService roomPersistentService;

    @Mock
    private UserRoomMappingPersistentService userRoomMappingPersistentService;

    @Mock
    private RoomMapper roomMapper;
    @Mock
    private UserMapper userMapper;

    @Mock
    private UserRoomMapper userRoomMapper;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RoomServiceImpl roomService;

    private DummyData dummyData;
    private User user;
    private Room room;
    private UserRoomMapping userRoomMapping;
    private RoomDTORequest roomDTORequest;
    private RoomDTOResponse roomDTOResponse;
    private JoinRoomDTOResponse joinRoomDTOResponse;
    private JoinRoomDTORequest joinRoomDTORequest;
    @Mock
    private Authentication authentication;
    @Mock
    private SecurityContext securityContext;


    @BeforeEach
    void setUp() {
        dummyData = new DummyData();
        setDummyData();
    }

    private void setDummyData(){
        user = dummyData.createUser();
        room = dummyData.createRoom();
        userRoomMapping = dummyData.createUserRoomMapping();
        roomDTORequest = dummyData.createRoomDTORequest();
        roomDTOResponse = dummyData.createRoomDTOResponse();
        joinRoomDTORequest = dummyData.createJoinRoomDTORequest();
        joinRoomDTOResponse = dummyData.createJoinRoomDTOResponse();
    }

    @Test
    void createRoom_Success() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("jay@gmail.com");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(roomPersistentService.getRoomByName(anyString())).thenReturn(Optional.empty());
        when(userRoomMappingPersistentService.getUserRoomMappingsByUserId(anyLong())).thenReturn(Optional.empty());
        when(roomPersistentService.saveRoom(any(Room.class))).thenReturn(room);
        when(userRoomMappingPersistentService.saveMapping(any(UserRoomMapping.class))).thenReturn(userRoomMapping);
        when(roomMapper.toRoomDTOResponse(any(Room.class))).thenReturn(roomDTOResponse);

        RoomDTOResponse response = roomService.createRoom(roomDTORequest);

        assertNotNull(response);
        assertEquals(roomDTOResponse, response);
        verify(roomPersistentService, times(2)).saveRoom(any(Room.class));
        verify(userRoomMappingPersistentService, times(1)).saveMapping(any(UserRoomMapping.class));
    }

    @Test
    void createRoom_Failure_RoomAlreadyExists() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("jay@gmail.com");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(roomPersistentService.getRoomByName(anyString())).thenReturn(Optional.of(room));

        CustomException exception = assertThrows(CustomException.class, () -> {
            roomService.createRoom(roomDTORequest);
        });

        assertEquals("400", exception.getErrorCode());
        assertEquals("Room already exists with the given name!", exception.getErrorMessage());
        verify(roomPersistentService, times(0)).saveRoom(any(Room.class));
    }


    @Test
    void createRoom_Failure_UserNotExists() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("jay@gmail.com");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> {
            roomService.createRoom(roomDTORequest);
        });

        assertEquals("400", exception.getErrorCode());
        assertEquals("User doesn't exist", exception.getErrorMessage());
    }


    @Test
    void createRoom_Failure_UserAlreadyInRoom() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("jay@gmail.com");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(roomPersistentService.getRoomByName(anyString())).thenReturn(Optional.empty());
        when(userRoomMappingPersistentService.getUserRoomMappingsByUserId(anyLong())).thenReturn(Optional.of(userRoomMapping));

        CustomException exception = assertThrows(CustomException.class, () -> {
            roomService.createRoom(roomDTORequest);
        });

        assertEquals("400", exception.getErrorCode());
        assertEquals("User is already in a room!", exception.getErrorMessage());
        verify(roomPersistentService, times(0)).saveRoom(any(Room.class));
    }

    @Test
    void getRoomById_Success() {
        when(roomPersistentService.getRoomById(anyInt())).thenReturn(Optional.of(room));
        when(roomMapper.toRoomDTOResponse(any(Room.class))).thenReturn(roomDTOResponse);

        RoomDTOResponse response = roomService.getRoomById(1);

        assertNotNull(response);
        assertEquals(roomDTOResponse, response);
    }

    @Test
    void getRoomById_Failure_RoomDoesNotExist() {
        when(roomPersistentService.getRoomById(anyInt())).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> {
            roomService.getRoomById(1);
        });

        assertEquals("400", exception.getErrorCode());
        assertEquals("Room doesn't exist!", exception.getErrorMessage());
    }

    @Test
    void updateRoom_Success() {
        when(roomPersistentService.getRoomById(anyInt())).thenReturn(Optional.of(room));
        when(roomPersistentService.saveRoom(any(Room.class))).thenReturn(room);
        when(roomMapper.toRoomDTOResponse(any(Room.class))).thenReturn(roomDTOResponse);

        RoomDTOResponse response = roomService.updateRoom(roomDTORequest);

        assertNotNull(response);
        assertEquals(roomDTOResponse, response);
        verify(roomPersistentService, times(1)).saveRoom(any(Room.class));
    }

    @Test
    void updateRoom_Failure_RoomDoesNotExist() {
        when(roomPersistentService.getRoomById(anyInt())).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> {
            roomService.updateRoom(roomDTORequest);
        });

        assertEquals("400", exception.getErrorCode());
        assertEquals("Room doesn't exist!", exception.getErrorMessage());
    }

    @Test
    void deleteRoom_Success() {
        when(roomPersistentService.getRoomById(anyInt())).thenReturn(Optional.of(room));

        roomService.deleteRoom(1);

        verify(roomPersistentService, times(1)).deleteRoomById(anyInt());
    }

    @Test
    void deleteRoom_Failure_RoomDoesNotExist() {
        when(roomPersistentService.getRoomById(anyInt())).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> {
            roomService.deleteRoom(1);
        });

        assertEquals("400", exception.getErrorCode());
        assertEquals("Room doesn't exist!", exception.getErrorMessage());
    }

    @Test
    void joinRoom_Success() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("jay@gmail.com");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(roomPersistentService.getRoomByCode(anyInt())).thenReturn(Optional.of(room));
        when(userRoomMappingPersistentService.getUserRoomMappingsByUserId(anyLong())).thenReturn(Optional.empty());
        when(roomPersistentService.saveRoom(any(Room.class))).thenReturn(room);
        when(userRoomMappingPersistentService.saveMapping(any(UserRoomMapping.class))).thenReturn(userRoomMapping);
        when(userRoomMapper.UserRoomToUserRoomDTOResponse(any(UserRoomMapping.class))).thenReturn(joinRoomDTOResponse);

        JoinRoomDTOResponse response = roomService.joinRoom(joinRoomDTORequest);

        assertNotNull(response);
        assertEquals(joinRoomDTOResponse, response);
    }

    @Test
    void joinRoom_Failure_IncorrectJoinCode() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("jay@gmail.com");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(roomPersistentService.getRoomByCode(anyInt())).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> {
            roomService.joinRoom(joinRoomDTORequest);
        });

        assertEquals("400", exception.getErrorCode());
        assertEquals("Incorrect join room code!", exception.getErrorMessage());
    }

    @Test
    void joinRoom_Failure_UserNotExist() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("jay@gmail.com");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> {
            roomService.joinRoom(joinRoomDTORequest);
        });

        assertEquals("400", exception.getErrorCode());
        assertEquals("User doesn't exist", exception.getErrorMessage());
    }


    @Test
    void joinRoom_Failure_UserAlreadyInRoom() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("jay@gmail.com");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(roomPersistentService.getRoomByCode(anyInt())).thenReturn(Optional.of(room));
        when(userRoomMappingPersistentService.getUserRoomMappingsByUserId(anyLong())).thenReturn(Optional.of(userRoomMapping));

        CustomException exception = assertThrows(CustomException.class, () -> {
            roomService.joinRoom(joinRoomDTORequest);
        });

        assertEquals("400", exception.getErrorCode());
        assertEquals("User already in room!", exception.getErrorMessage());
    }

    @Test
    void leaveRoom_Success() {
        doNothing().when(userRoomMappingPersistentService).deleteUserRoomMapping(any(UserRoomMapping.class));
        when(roomPersistentService.saveRoom(any(Room.class))).thenReturn(room);

        roomService.leaveRoom(user, room, userRoomMapping);

        verify(userRoomMappingPersistentService, times(1)).deleteUserRoomMapping(any(UserRoomMapping.class));
        verify(roomPersistentService, times(1)).saveRoom(any(Room.class));
    }

    @Test
    void getAllUsersByRoom_Success() {

        int roomId = 1;
        UserRoomMapping userRoomMapping1 = dummyData.createUserRoomMapping();
        UserRoomMapping userRoomMapping2 = dummyData.createUserRoomMapping();
        User user1 = dummyData.createUser();
        User user2 = dummyData.createUser();

        userRoomMapping1.setUserid(user1);
        userRoomMapping2.setUserid(user2);

        List<UserRoomMapping> userRoomMappings = List.of(userRoomMapping1, userRoomMapping2);

        when(userRoomMappingPersistentService.getAllUsersByRoom(roomId)).thenReturn(Optional.of(userRoomMappings));
        when(userMapper.toUserDTOResponse(user1)).thenReturn(new UserDTOResponse());
        when(userMapper.toUserDTOResponse(user2)).thenReturn(new UserDTOResponse());

        List<UserDTOResponse> result = roomService.getAllUsersByRoom(roomId);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(userRoomMappingPersistentService, times(1)).getAllUsersByRoom(roomId);
        verify(userMapper, times(2)).toUserDTOResponse(any(User.class));
    }

    @Test
    void getAllUsersByRoom_Empty() {
        int roomId = 1;

        when(userRoomMappingPersistentService.getAllUsersByRoom(roomId)).thenReturn(Optional.empty());

        List<UserDTOResponse> result = roomService.getAllUsersByRoom(roomId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(userRoomMappingPersistentService, times(1)).getAllUsersByRoom(roomId);
        verify(userMapper, times(0)).toUserDTOResponse(any(User.class));
    }
}