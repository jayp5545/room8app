package com.ASDC.backend.service;

import com.ASDC.backend.dto.ResetPasswordDTO;
import com.ASDC.backend.entity.PasswordResetToken;
import com.ASDC.backend.entity.User;
import com.ASDC.backend.repository.PasswordResetTokenRepository;
import com.ASDC.backend.repository.UserRepository;
import com.ASDC.backend.service.implementation.EmailServiceImpl;
import com.ASDC.backend.service.implementation.ForgotPasswordServiceImpl;
import com.ASDC.backend.service.implementation.TokenServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ForgotPasswordServiceTest
{
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Mock
    private EmailServiceImpl emailServiceImpl;

    @Mock
    private TokenServiceImpl tokenServiceImpl;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private ForgotPasswordServiceImpl forgotPasswordServiceImpl;

    private static User existingUser;
    private static String existingEmail;
    private static String validToken;
    private static ResetPasswordDTO resetPasswordDTO;

    @BeforeAll
    public static void setup()
    {
        existingEmail = "existing@example.com";
        existingUser = User.builder().id(1L).firstName("test").lastName("test").email(existingEmail).password("test123").build();
        validToken = "validToken";
        resetPasswordDTO = new ResetPasswordDTO(validToken, "newPassword","newPassword");
    }

    // Tests for forgotPassword method

    @Test
    void testForgotPassword_WithExistingEmail_ShouldReturnTrue()
    {
        when(userRepository.findByEmail(existingEmail)).thenReturn(java.util.Optional.of(existingUser));
        when(tokenServiceImpl.generateToken()).thenReturn(validToken);

        boolean result = forgotPasswordServiceImpl.forgotPassword(existingEmail);

        assertTrue(result);
        verify(emailServiceImpl).sendPasswordResetEmail(existingEmail, validToken);
        verify(tokenServiceImpl).saveToken(validToken, existingUser);
    }

    @Test
    void testForgotPassword_WithNonExistingEmail_ShouldReturnFalse()
    {
        when(userRepository.findByEmail(anyString())).thenReturn(java.util.Optional.empty());

        boolean result = forgotPasswordServiceImpl.forgotPassword(existingEmail);

        assertFalse(result);
        verify(emailServiceImpl, never()).sendPasswordResetEmail(anyString(), anyString());
        verify(tokenServiceImpl, never()).saveToken(anyString(), any(User.class));
    }

    // Tests for resetPassword method

    @Test
    void testResetPassword_WithValidToken_ShouldReturnTrue()
    {
        PasswordResetToken passwordResetToken = new PasswordResetToken(1L,validToken, existingUser, LocalDateTime.now().plusMinutes(5));
        when(passwordResetTokenRepository.findByToken(validToken)).thenReturn(Optional.of(passwordResetToken));

        when(tokenServiceImpl.validToken(validToken)).thenReturn(true);
        when(passwordEncoder.encode(anyString())).thenReturn("ENCODED_PASSWORD");

        boolean result = forgotPasswordServiceImpl.resetPassword(resetPasswordDTO);

        assertTrue(result);

        verify(userRepository).save(existingUser);
        verify(passwordResetTokenRepository).delete(passwordResetToken);
    }

    @Test
    void testResetPassword_WithInvalidToken_ShouldReturnTrue()
    {
        boolean result = forgotPasswordServiceImpl.resetPassword(resetPasswordDTO);

        assertFalse(result);
        verify(userRepository, never()).save(any(User.class));
        verify(passwordResetTokenRepository, never()).delete(any(PasswordResetToken.class));
    }

    // Tests for matchPasswords method

    @Test
    void matchPasswords_WithMatchingPasswords_ShouldReturnTrue()
    {
        boolean result = forgotPasswordServiceImpl.matchPasswords(resetPasswordDTO);
        assertTrue(result);
    }

    @Test
    void matchPasswords_WithNotMatchingPasswords_ShouldReturnFalse()
    {
        resetPasswordDTO.setPassword("123");
        boolean result = forgotPasswordServiceImpl.matchPasswords(resetPasswordDTO);
        assertFalse(result);
    }
}
