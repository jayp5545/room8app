package com.ASDC.backend.service;

import com.ASDC.backend.DummyData.DummyData;
import com.ASDC.backend.dto.RequestDTO.ProfileDTORequest;
import com.ASDC.backend.dto.ResponseDTO.ProfileDTOResponse;
import com.ASDC.backend.dto.ResponseDTO.UserDTOResponse;
import com.ASDC.backend.entity.Room;
import com.ASDC.backend.entity.User;
import com.ASDC.backend.entity.UserRoomMapping;
import com.ASDC.backend.exception.CustomException;
import com.ASDC.backend.mapper.RoomMapper;
import com.ASDC.backend.mapper.UserMapper;
import com.ASDC.backend.repository.UserRepository;
import com.ASDC.backend.service.implementation.ProfileServiceImpl;
import com.ASDC.backend.service.PersistentServices.RoomPersistentService;
import com.ASDC.backend.service.PersistentServices.UserRoomMappingPersistentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProfileServiceImplTest {

    private DummyData dummyData;
    private User user;
    private Room room;
    private UserRoomMapping userRoomMapping;
    private ProfileDTOResponse profileDTOResponse;
    private ProfileDTORequest profileDTORequest;

    @Mock
    UserRepository userRepository;

    @Mock
    UserMapper userMapper;

    @Mock
    RoomMapper roomMapper;

    @Mock
    RoomPersistentService roomPersistentService;

    @Mock
    UserRoomMappingPersistentService userRoomMappingPersistentService;

    @InjectMocks
    ProfileServiceImpl profileService;

    @BeforeEach
    public void setUp() {
        dummyData = new DummyData();
        setDummyData();
    }

    private void setDummyData() {
        user = dummyData.createUser();
        room = dummyData.createRoom();
        userRoomMapping = dummyData.createUserRoomMapping();
        profileDTOResponse = dummyData.createProfileDTOResponse();
        profileDTORequest = dummyData.createProfileDTORequest();
    }

    @Test
    public void changeName_Success() {
        String email = "test@example.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toUserDTOResponse(any(User.class))).thenReturn(profileDTOResponse.getUserDTOResponse());

        UserDTOResponse result = profileService.changeName(email, profileDTORequest);

        assertNotNull(result);
        assertEquals("Jay Sanjaybhai", result.getFirstName());
        assertEquals("Patel", result.getLastName());
        verify(userRepository, times(1)).findByEmail(email);
        verify(userRepository, times(1)).save(any(User.class));
        verify(userMapper, times(1)).toUserDTOResponse(any(User.class));
    }

    @Test
    public void getProfile_Success() {
        String email = "test@example.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(userRoomMappingPersistentService.getUserRoomMappingsByUserId(user.getId())).thenReturn(Optional.of(userRoomMapping));
        when(roomPersistentService.getRoomById(userRoomMapping.getRoomid().getId())).thenReturn(Optional.of(room));
        when(userMapper.toUserDTOResponse(any(User.class))).thenReturn(profileDTOResponse.getUserDTOResponse());
        when(roomMapper.toRoomDTOResponse(any(Room.class))).thenReturn(profileDTOResponse.getRoomDTOResponse());

        ProfileDTOResponse result = profileService.getProfile(email);

        assertNotNull(result);
        assertNotNull(result.getUserDTOResponse());
        assertNotNull(result.getRoomDTOResponse());
        assertEquals(user.getFirstName(), result.getUserDTOResponse().getFirstName());
        verify(userRepository, times(1)).findByEmail(email);
        verify(userRoomMappingPersistentService, times(1)).getUserRoomMappingsByUserId(user.getId());
        verify(roomPersistentService, times(1)).getRoomById(userRoomMapping.getRoomid().getId());
        verify(userMapper, times(1)).toUserDTOResponse(any(User.class));
        verify(roomMapper, times(1)).toRoomDTOResponse(any(Room.class));
    }


    @Test
    public void getProfile_Success_UserNotInRoom() {
        String email = "test@example.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(userRoomMappingPersistentService.getUserRoomMappingsByUserId(user.getId())).thenReturn(Optional.empty());

        ProfileDTOResponse result = profileService.getProfile(email);

        assertNotNull(result);
        assertNull(result.getUserDTOResponse());
        assertNull(result.getRoomDTOResponse());
        verify(userRepository, times(1)).findByEmail(email);
        verify(userRoomMappingPersistentService, times(1)).getUserRoomMappingsByUserId(user.getId());
        verify(roomPersistentService, times(0)).getRoomById(userRoomMapping.getRoomid().getId());
    }


    @Test
    public void getProfile_UserNotFound() {
        String email = "test@example.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> {
            profileService.getProfile(email);
        });

        assertEquals("400", exception.getErrorCode());
        assertEquals("User doesn't exist!", exception.getErrorMessage());
        verify(userRepository, times(1)).findByEmail(email);
    }
}