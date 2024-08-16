package com.ASDC.backend.service;

import com.ASDC.backend.DummyData.DummyData;
import com.ASDC.backend.entity.User;
import com.ASDC.backend.repository.UserRepository;
import com.ASDC.backend.service.PersistentServices.UserPersistentService;
import lombok.Data;
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
public class UserPersistentServiceTest {

    @InjectMocks
    private UserPersistentService userPersistentService;

    @Mock
    private UserRepository userRepository;

    private DummyData dummyData;

    private User user;

    @BeforeEach
    public void setup() {
        dummyData = new DummyData();
        user = dummyData.createUserForUserService();
    }

    @Test
    void getUserByEmail_UserExists() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        User foundUser = userPersistentService.getUserByEmail(user.getEmail());

        assertNotNull(foundUser);
        assertEquals(user.getEmail(), foundUser.getEmail());
        verify(userRepository, times(1)).findByEmail(user.getEmail());
    }

    @Test
    void getUserByEmail_UserDoesNotExist() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

        User foundUser = userPersistentService.getUserByEmail(user.getEmail());

        assertNull(foundUser);
        verify(userRepository, times(1)).findByEmail(user.getEmail());
    }

    @Test
    void saveUser_Success() {
        when(userRepository.save(any(User.class))).thenReturn(user);

        User savedUser = userPersistentService.saveUser(user);

        assertNotNull(savedUser);
        assertEquals(user.getEmail(), savedUser.getEmail());
        verify(userRepository, times(1)).save(user);
    }
}
