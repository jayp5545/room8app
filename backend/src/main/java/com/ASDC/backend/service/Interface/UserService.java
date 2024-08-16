package com.ASDC.backend.service.Interface;
import com.ASDC.backend.dto.ResponseDTO.AuthDTOResponse;
import com.ASDC.backend.dto.UserDTO;
import com.ASDC.backend.entity.User;

public interface UserService {

    User getUserByEmail(String email);

    UserDTO createUser(UserDTO userDTO);

    AuthDTOResponse loginUser(UserDTO userDTO);
}