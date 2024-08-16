package com.ASDC.backend.controller;

import com.ASDC.backend.dto.UserDTO;
import com.ASDC.backend.filter.Interceptor;
import com.ASDC.backend.mapper.RoomMapper;
import com.ASDC.backend.repository.RoomRepository;
import com.ASDC.backend.repository.UserRoomRepository;
import com.ASDC.backend.service.Interface.UserService;
import com.ASDC.backend.dto.ResponseDTO.AuthDTOResponse;
import com.ASDC.backend.util.JWTUtil;
import io.jsonwebtoken.ExpiredJwtException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin
public class AuthController {

    private static final Logger logger = LogManager.getLogger(AuthController.class);

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final Interceptor interceptor;
    private final UserRoomRepository userRoomRepository;
    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;
    private final JWTUtil jwtUtil;

    public AuthController(UserService userService, AuthenticationManager authenticationManager, Interceptor interceptor, UserRoomRepository userRoomRepository, RoomRepository roomRepository, RoomMapper roomMapper, JWTUtil jwtUtil) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.interceptor = interceptor;
        this.userRoomRepository = userRoomRepository;
        this.roomRepository = roomRepository;
        this.roomMapper = roomMapper;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDTO userDTO) {
        logger.info("Register request received for email: {}", userDTO.getEmail());
        try {
            UserDTO createdUser = userService.createUser(userDTO);
            logger.info("User registered successfully with email: {}", createdUser.getEmail());
            return ResponseEntity.ok(createdUser);
        } catch (ResponseStatusException e) {
            logger.error("Error registering user: {}", e.getReason(), e);
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDTO userDTO){
        logger.info("Login request received for email: {}", userDTO.getEmail());
        try {
            AuthDTOResponse authResponse = userService.loginUser(userDTO);
            logger.info("User logged in successfully with email: {}", userDTO.getEmail());
            return ResponseEntity.ok(authResponse);
        } catch (BadCredentialsException e) {
            logger.error("Invalid credentials for email: {}", userDTO.getEmail(), e);
            return ResponseEntity.badRequest().body("Invalid username or password");
        }
    }

    @PostMapping("/verify-token")
    public ResponseEntity<?> verifyToken(@RequestHeader("Authorization") String token) {
        logger.info("Token verification request received");
        try {
            String accessToken = token.substring(7);
            boolean isValid = jwtUtil.validateToken(accessToken);
            return ResponseEntity.ok(new HashMap<String, String>() {{
                put("tokenValid", "true");
            }});
        } catch(ExpiredJwtException e){
            return ResponseEntity.ok(new HashMap<String, String>() {{
                put("tokenValid", "false");
            }});
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new HashMap<String, String>() {{
                put("tokenValid", "false");
            }});
        }
    }
}

