package com.ASDC.backend.service;

import com.ASDC.backend.config.CustomUserDetails;
import com.ASDC.backend.entity.User;
import com.ASDC.backend.repository.UserRepository;
import com.ASDC.backend.service.implementation.UserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepo;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void loadUserByUsernameTest() {
        User user = new User();
        user.setId(1L);
        user.setFirstName("Test");
        user.setLastName("Test1");
        user.setEmail("test@test.com");
        user.setPassword("testPass");

        when(userRepo.findByEmail("test@test.com")).thenReturn(Optional.of(user));

        CustomUserDetails customUserDetails = (CustomUserDetails) userDetailsService.loadUserByUsername("test@test.com");

        assertEquals("test@test.com", customUserDetails.getUsername());
        assertEquals("testPass", customUserDetails.getPassword());
    }

    @Test
    public void loadUserByUsernameNotFoundTest() {
        when(userRepo.findByEmail("test@test.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("test@test.com");
        });
    }
}
