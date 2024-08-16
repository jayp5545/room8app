package com.ASDC.backend.service;

import com.ASDC.backend.entity.PasswordResetToken;
import com.ASDC.backend.entity.User;
import com.ASDC.backend.repository.PasswordResetTokenRepository;
import com.ASDC.backend.service.implementation.TokenServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TokenServiceImplTest
{
    @Mock
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @InjectMocks
    private TokenServiceImpl tokenServiceImpl;

    private static User testUser;

    @BeforeAll
    static void setup()
    {
        Long testUserId = 99L;
        String testUserFirstName = "dummyFirstName";
        String testUserLastName = "dummyLastName";
        String testUserEmail = "abc@example.com";
        String testUserPassword = "123";

        testUser = User.builder().id(testUserId).firstName(testUserFirstName).lastName(testUserLastName).email(testUserEmail).password(testUserPassword).build();
    }

    // Tests for generateToken method
    @Test
    void testGenerateToken_returnsValidToken()
    {
        String token = tokenServiceImpl.generateToken();

        assertNotNull(token);

        // Doing it because UUID is supposed to be 36 characters in length
        assertEquals(36, token.length());
    }

    // Tests for isTokenExpired method
    @Test
    public void testIsTokenExpired_tokenExpired_returnsTrue()
    {
        LocalDateTime expiredTime = LocalDateTime.now().minusMinutes(2);
        assertTrue(tokenServiceImpl.isTokenExpired(expiredTime));
    }

    @Test
    public void testIsTokenExpired_tokenNotExpired_returnsFalse()
    {
        LocalDateTime futureTime = LocalDateTime.now().plusMinutes(1);
        assertFalse(tokenServiceImpl.isTokenExpired(futureTime));
    }

    // Tests for isTokenPresent method
    @Test
    public void testIsTokenPresent_tokenFound_returnsTrue()
    {
        String token = "test_token";
        when(passwordResetTokenRepository.findByToken(token)).thenReturn(Optional.of(new PasswordResetToken()));
        assertTrue(tokenServiceImpl.isTokenPresent(token));
    }

    @Test
    public void testIsTokenPresent_tokenNotFound_returnsFalse()
    {
        String token = "test_token";
        when(passwordResetTokenRepository.findByToken(token)).thenReturn(Optional.empty());
        assertFalse(tokenServiceImpl.isTokenPresent(token));
    }

    // Tests for validToken method

    @Test
    public void testValidToken_tokenValid_returnsTrue()
    {
        String token = "test_token";
        LocalDateTime futureTime = LocalDateTime.now().plusMinutes(2);
        when(passwordResetTokenRepository.findByToken(token)).thenReturn(Optional.of(new PasswordResetToken(1L,token, testUser, futureTime)));
        assertTrue(tokenServiceImpl.validToken(token));
    }

    @Test
    public void testValidToken_tokenNotFound_returnsFalse()
    {
        String token = "test_token";
        when(passwordResetTokenRepository.findByToken(token)).thenReturn(Optional.empty());
        assertFalse(tokenServiceImpl.validToken(token));
    }

    @Test
    public void testValidToken_tokenExpired_returnsFalse()
    {
        String token = "test_token";
        LocalDateTime expiredTime = LocalDateTime.now().minusMinutes(2);
        when(passwordResetTokenRepository.findByToken(token)).thenReturn(Optional.of(new PasswordResetToken(1L,token, testUser, expiredTime)));
        assertFalse(tokenServiceImpl.validToken(token));
    }

    // Tests for saveToken method

    @Test
    public void testSaveToken_savesTokenToRepository()
    {
        String token = "test_token";
        ArgumentCaptor<PasswordResetToken> passwordResetTokenCaptor = ArgumentCaptor.forClass(PasswordResetToken.class);

        tokenServiceImpl.saveToken(token, testUser);

        verify(passwordResetTokenRepository).save(passwordResetTokenCaptor.capture());
        PasswordResetToken capturedToken = passwordResetTokenCaptor.getValue();
        assertEquals(token, capturedToken.getToken());
        assertEquals(testUser, capturedToken.getUser());
        assertNotNull(capturedToken.getExpiresAt());
    }
}
