package com.ASDC.backend.service.implementation;

import com.ASDC.backend.entity.PasswordResetToken;
import com.ASDC.backend.entity.User;
import com.ASDC.backend.repository.PasswordResetTokenRepository;
import com.ASDC.backend.service.Interface.TokenService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class TokenServiceImpl implements TokenService {
    private final int EXPIRY_TIME_IN_MINUTES = 30;
    private PasswordResetTokenRepository passwordResetTokenRepository;

    public TokenServiceImpl(PasswordResetTokenRepository passwordResetTokenRepository)
    {
        this.passwordResetTokenRepository = passwordResetTokenRepository;
    }

    @Override
    public String generateToken()
    {
        String token = UUID.randomUUID().toString();
        return token;
    }

    @Override
    public boolean isTokenExpired(LocalDateTime tokenExpiry)
    {
        LocalDateTime now = LocalDateTime.now();

        boolean expiredToken = tokenExpiry.isBefore(now);
        return expiredToken;
    }

    @Override
    public boolean isTokenPresent(String token)
    {
        Optional<PasswordResetToken> passwordResetTokenOptional = passwordResetTokenRepository.findByToken(token);
        boolean tokenNotFound = passwordResetTokenOptional.isEmpty();

        if(tokenNotFound)
        {
            return false;
        }

        return true;
    }

    @Override
    public boolean validToken(String token)
    {
        boolean tokenFound = isTokenPresent(token);
        if(!tokenFound)
        {
            return false;
        }

        LocalDateTime tokenExpiry = passwordResetTokenRepository.findByToken(token).get().getExpiresAt();

        boolean tokenExpired = isTokenExpired(tokenExpiry);
        if(tokenExpired)
        {
            return false;
        }

        return true;
    }

    @Override
    public void saveToken(String token, User user)
    {
        LocalDateTime tokenExpiry = LocalDateTime.now().plusMinutes(EXPIRY_TIME_IN_MINUTES);
        PasswordResetToken passwordResetToken = PasswordResetToken.builder().token(token).user(user).expiresAt(tokenExpiry).build();
        passwordResetTokenRepository.save(passwordResetToken);
    }

}
