package com.ASDC.backend.dto.ResponseDTO;

import com.ASDC.backend.entity.UserRoomMapping;
import lombok.*;

import java.time.LocalDateTime;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileDTOResponse {
    private UserDTOResponse userDTOResponse;
    private RoomDTOResponse roomDTOResponse;
    private LocalDateTime joinDate;
}
