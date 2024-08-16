package com.ASDC.backend.service.Interface;

import com.ASDC.backend.dto.RequestDTO.ProfileDTORequest;
import com.ASDC.backend.dto.ResponseDTO.ProfileDTOResponse;
import com.ASDC.backend.dto.ResponseDTO.UserDTOResponse;
import com.ASDC.backend.entity.Room;
import com.ASDC.backend.entity.User;
import com.ASDC.backend.entity.UserRoomMapping;

public interface ProfileService {
    UserDTOResponse changeName(String email, ProfileDTORequest profileDTORequest);
    ProfileDTOResponse getProfile(String email);
}
