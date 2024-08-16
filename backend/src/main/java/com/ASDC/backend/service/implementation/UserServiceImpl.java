package com.ASDC.backend.service.implementation;

import com.ASDC.backend.dto.ResponseDTO.AuthDTOResponse;
import com.ASDC.backend.dto.UserDTO;
import com.ASDC.backend.entity.Room;
import com.ASDC.backend.entity.User;
import com.ASDC.backend.entity.UserRoomMapping;
import com.ASDC.backend.exception.CustomException;
import com.ASDC.backend.mapper.RoomMapper;
import com.ASDC.backend.repository.RoomRepository;
import com.ASDC.backend.repository.UserRepository;
import com.ASDC.backend.repository.UserRoomRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.ASDC.backend.service.Interface.UserService;
import com.ASDC.backend.service.PersistentServices.UserPersistentService;
import com.ASDC.backend.service.PersistentServices.UserRoomMappingPersistentService;
import com.ASDC.backend.util.JWTUtil;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);

    private final UserRepository userRepo;

    private final ModelMapper modelMapper;

    private final PasswordEncoder passwordEncoder;

    private final UserPersistentService userPersistentService;

    private final JWTUtil jwtUtil;

    private final AuthenticationManager authenticationManager;


    private final UserRoomRepository userRoomRepository;

    private final RoomRepository roomRepository;

    private final RoomMapper roomMapper;

    private final UserRoomMappingPersistentService userRoomMappingPersistentService;


    public UserServiceImpl(UserRepository userRepo, ModelMapper modelMapper, PasswordEncoder passwordEncoder, UserPersistentService userPersistentService, JWTUtil jwtUtil, AuthenticationManager authenticationManager, UserRoomRepository userRoomRepository, RoomRepository roomRepository, RoomMapper roomMapper, UserRoomMappingPersistentService userRoomMappingPersistentService) {
        this.userRepo = userRepo;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.userPersistentService = userPersistentService;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.userRoomRepository = userRoomRepository;
        this.roomRepository = roomRepository;
        this.roomMapper = roomMapper;
        this.userRoomMappingPersistentService = userRoomMappingPersistentService;
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepo.findByEmail(email)
                .orElseThrow(() -> new CustomException("400", "User with email " + email + " does not exist."));
    }

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        logger.info("Creating user with email: {}", userDTO.getEmail());
        // Checking if the email is already used by another user
        if(checkIfUserExists(userDTO.getEmail())){
            logger.error("Email already in use: {}", userDTO.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already in use");
        }

        // Converting the DTO to User object
        User user = modelMapper.map(userDTO, User.class);

        String userPassword = user.getPassword();

        // Encoding the password
        user.setPassword(passwordEncoder.encode(userPassword));
        // Saving the user
        User savedUser = saveUser(user);
        logger.info("User created successfully with email: {}", savedUser.getEmail());
        return modelMapper.map(savedUser,UserDTO.class);
    }

    @Override
    public AuthDTOResponse loginUser(UserDTO userDTO) {
        logger.info("Authenticating user with email: {}", userDTO.getEmail());
        Authentication authentication = authenticateUser(userDTO);
        String token = generateToken(userDTO.getEmail());
        AuthDTOResponse authResponse = buildAuthResponse(userDTO.getEmail(), token);
        setRoomDetails(authResponse, userDTO.getEmail());
        logger.info("User authenticated successfully with email: {}", userDTO.getEmail());
        return authResponse;
    }

    private Authentication authenticateUser(UserDTO userDTO) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDTO.getEmail(), userDTO.getPassword());
        return authenticationManager.authenticate(authenticationToken);
    }

    private String generateToken(String email) {
        return jwtUtil.generateToken(email);
    }

    private AuthDTOResponse buildAuthResponse(String email, String token) {
        return AuthDTOResponse.builder()
                .email(email)
                .token(token)
                .build();
    }

    private void setRoomDetails(AuthDTOResponse authResponse, String email) {
        User user = userPersistentService.getUserByEmail(email);
        Optional<UserRoomMapping> userRoom = userRoomMappingPersistentService.getUserRoomMappingsByUserId(user.getId());

        Optional<Room> room = userRoom
                .map(urm -> roomRepository.findById(urm.getRoomid().getId()))
                .orElse(Optional.empty());

        authResponse.setRoomDTOResponse(
                room.map(r -> roomMapper.toRoomDTOResponse(r))
                        .orElse(null)
        );
    }
    private boolean checkIfUserExists(String email){
        logger.info("Checking if user exists with email: {}", email);
        User user = userPersistentService.getUserByEmail(email);
        if(user != null) {
            return true;
        }
        return false;
    }

    private User saveUser(User userToSave){
        logger.info("Saving user with email: {}", userToSave.getEmail());
        User savedUser = userPersistentService.saveUser(userToSave);
        return savedUser;
    }
}
