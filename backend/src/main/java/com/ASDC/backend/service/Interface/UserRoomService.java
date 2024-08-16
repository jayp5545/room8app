package com.ASDC.backend.service.Interface;

import com.ASDC.backend.entity.User;
import com.ASDC.backend.entity.UserRoomMapping;

public interface UserRoomService {

    UserRoomMapping getUserRoom(User user);

}
