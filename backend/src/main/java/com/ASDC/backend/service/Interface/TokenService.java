package com.ASDC.backend.service.Interface;

import com.ASDC.backend.entity.User;

import java.time.LocalDateTime;

public interface TokenService {

    String generateToken();
    boolean isTokenExpired(LocalDateTime tokenExpiry);
    boolean isTokenPresent(String token);
    boolean validToken(String token);
    void saveToken(String token, User user);

}
