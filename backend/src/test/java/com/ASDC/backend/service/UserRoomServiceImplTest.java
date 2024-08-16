package com.ASDC.backend.service;
import com.ASDC.backend.entity.User;
import com.ASDC.backend.entity.UserRoomMapping;
import com.ASDC.backend.exception.CustomException;
import com.ASDC.backend.repository.UserRoomRepository;
import com.ASDC.backend.service.implementation.UserRoomServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class UserRoomServiceImplTest {

    @Mock
    private UserRoomRepository userRoomRepository;

    @InjectMocks
    private UserRoomServiceImpl userRoomService;


    @Test
    void testGetUserRoom_UserRoomExists() {
        User user = new User();
        user.setId(1L);

        UserRoomMapping userRoomMapping = new UserRoomMapping();
        when(userRoomRepository.findByUserID(user.getId())).thenReturn(Optional.of(userRoomMapping));

        UserRoomMapping result = userRoomService.getUserRoom(user);

        assertNotNull(result);
        assertEquals(userRoomMapping, result);
        verify(userRoomRepository, times(1)).findByUserID(user.getId());
    }

    @Test
    void testGetUserRoom_UserRoomDoesNotExist() {
        User user = new User();
        user.setId(1L);
        when(userRoomRepository.findByUserID(user.getId())).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> {
            userRoomService.getUserRoom(user);
        });

        assertEquals("400", exception.getErrorCode());
        assertEquals("User Room data with userID 1 does not exist.", exception.getErrorMessage());
    }
}
