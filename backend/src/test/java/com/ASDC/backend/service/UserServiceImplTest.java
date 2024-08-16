package com.ASDC.backend.service;

import com.ASDC.backend.DummyData.DummyData;
import com.ASDC.backend.dto.ResponseDTO.AuthDTOResponse;
import com.ASDC.backend.dto.ResponseDTO.RoomDTOResponse;
import com.ASDC.backend.dto.UserDTO;
import com.ASDC.backend.entity.Room;
import com.ASDC.backend.entity.User;
import com.ASDC.backend.entity.UserRoomMapping;
import com.ASDC.backend.mapper.RoomMapper;
import com.ASDC.backend.repository.RoomRepository;
import com.ASDC.backend.repository.UserRepository;
import com.ASDC.backend.service.PersistentServices.UserPersistentService;
import com.ASDC.backend.service.PersistentServices.UserRoomMappingPersistentService;
import com.ASDC.backend.service.implementation.UserServiceImpl;
import com.ASDC.backend.util.JWTUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UserRepository userRepo;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private BCryptPasswordEncoder passwordEncoder;
    @Mock
    private UserPersistentService userPersistentService;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JWTUtil jwtUtil;
    @Mock
    private AuthDTOResponse authDTOResponse;
    @Mock
    private UserRoomMapping userRoomMapping;
    @Mock
    private Room room;
    @Mock
    private RoomDTOResponse roomDTOResponse;
    @Mock
    private UserRoomMappingPersistentService userRoomMappingPersistentService;
    @Mock
    private RoomRepository roomRepository;
    @Mock
    private RoomMapper roomMapper;
    DummyData dummyData;

    @BeforeEach
    public void setup() {
        dummyData = new DummyData();
    }

    @Test
    void createUserTest() {
        UserDTO userDTO = dummyData.createUserDTO();
        User user = dummyData.createUserForUserService();

        when(userPersistentService.getUserByEmail(userDTO.getEmail())).thenReturn(null);
        when(modelMapper.map(userDTO, User.class)).thenReturn(user);
        when(passwordEncoder.encode(userDTO.getPassword())).thenReturn("encodedPass");
        when(userPersistentService.saveUser(user)).thenReturn(user);
        when(modelMapper.map(user, UserDTO.class)).thenReturn(userDTO);


        UserDTO createdUser = userService.createUser(userDTO);

        assertNotNull(createdUser);
        assertEquals(userDTO.getEmail(), createdUser.getEmail());
        verify(userPersistentService, times(1)).getUserByEmail(userDTO.getEmail());
        verify(userPersistentService, times(1)).saveUser(user);
    }

    @Test
    void createUserTest_EmailAlreadyExists() {
        UserDTO userDTO = dummyData.createUserDTO();
        User existingUser = dummyData.createUserForUserService();

        when(userPersistentService.getUserByEmail(userDTO.getEmail())).thenReturn(existingUser);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            userService.createUser(userDTO);
        });

        assertEquals("400 BAD_REQUEST \"Email already in use\"", exception.getMessage());
        verify(userPersistentService, times(1)).getUserByEmail(userDTO.getEmail());
        verify(userPersistentService, times(0)).saveUser(any(User.class));
    }

    @Test
    void testLoginUser_Success() {
        // Arrange
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("test@example.com");
        userDTO.setPassword("password");

        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);

        when(jwtUtil.generateToken(userDTO.getEmail())).thenReturn("mockedToken");

        User user = new User();
        user.setId(1L);
        when(userPersistentService.getUserByEmail(userDTO.getEmail())).thenReturn(user);

        UserRoomMapping userRoomMapping = new UserRoomMapping();
        Room room = new Room();
        room.setId(1);
        userRoomMapping.setRoomid(room);
        when(userRoomMappingPersistentService.getUserRoomMappingsByUserId(user.getId())).thenReturn(Optional.of(userRoomMapping));
        when(roomRepository.findById(room.getId())).thenReturn(Optional.of(room));

        AuthDTOResponse response = userService.loginUser(userDTO);

        assertNotNull(response);
        assertEquals(userDTO.getEmail(), response.getEmail());
        assertEquals("mockedToken", response.getToken());
        verify(roomMapper).toRoomDTOResponse(room);
    }


    @Test
    void loginUserTest_InvalidCredentials() {
        UserDTO userDTO = dummyData.createUserDTO();
        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException(""));

        BadCredentialsException exception = assertThrows(BadCredentialsException.class, () -> {
            userService.loginUser(userDTO);
        });

        assertNotNull(exception);
        verify(authenticationManager, times(1)).authenticate(any());
        verify(jwtUtil, times(0)).generateToken(anyString());
    }

    @Test
    void testLoginUser_AuthenticationFailed() {
        // Arrange
        UserDTO userDTO = dummyData.createUserDTO();

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new org.springframework.security.authentication.BadCredentialsException("Bad credentials"));

        assertThrows(org.springframework.security.authentication.BadCredentialsException.class, () -> {
            userService.loginUser(userDTO);
        });
    }
    @Test
    void getUserByEmailTest_Success() {
        User user = dummyData.createUserForUserService();
        when(userRepo.findByEmail(anyString())).thenReturn(Optional.of(user));

        User foundUser = userService.getUserByEmail(user.getEmail());

        assertNotNull(foundUser);
        assertEquals(user.getEmail(), foundUser.getEmail());
        verify(userRepo, times(1)).findByEmail(user.getEmail());
    }

    @Test
    void testLoginUser_NoRoomAssigned() {
        UserDTO userDTO = dummyData.createUserDTO();

        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);

        when(jwtUtil.generateToken(userDTO.getEmail())).thenReturn("mockedToken");

        User user = dummyData.createUserForUserService();
        user.setId(1L);
        when(userPersistentService.getUserByEmail(userDTO.getEmail())).thenReturn(user);

        when(userRoomMappingPersistentService.getUserRoomMappingsByUserId(user.getId())).thenReturn(Optional.empty());

        AuthDTOResponse response = userService.loginUser(userDTO);

        assertNotNull(response);
        assertEquals(userDTO.getEmail(), response.getEmail());
        assertEquals("mockedToken", response.getToken());
        assertNull(response.getRoomDTOResponse());
    }

}