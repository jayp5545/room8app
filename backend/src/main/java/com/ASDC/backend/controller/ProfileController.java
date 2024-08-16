package com.ASDC.backend.controller;

import com.ASDC.backend.dto.RequestDTO.ProfileDTORequest;
import com.ASDC.backend.dto.ResponseDTO.ProfileDTOResponse;
import com.ASDC.backend.dto.ResponseDTO.UserDTOResponse;
import com.ASDC.backend.service.implementation.ProfileServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for handling profile-related API endpoints.
 */
@RestController
@RequestMapping("/api/v1/profile")
@RequiredArgsConstructor
public class ProfileController {

    private static final Logger logger = LogManager.getLogger(ProfileController.class);
    private final ProfileServiceImpl profileService;

    /**
     * Get the profile of the authenticated user.
     *
     * @return the profile DTO response
     */
    @GetMapping("/get")
    public ResponseEntity<ProfileDTOResponse> getUserProfile() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Fetching profile for user with email: {}", email);
        ProfileDTOResponse response = profileService.getProfile(email);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Update the name of the authenticated user.
     *
     * @param requestedProfile the profile request DTO containing the new name
     * @return the user DTO response
     */
    @PostMapping("/update")
    public ResponseEntity<UserDTOResponse> changeName(@RequestBody ProfileDTORequest requestedProfile) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Updating profile name for user with email: {}", email);
        UserDTOResponse response = profileService.changeName(email, requestedProfile);
        logger.info("Updated profile name for user with email: {}", email);
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }
}