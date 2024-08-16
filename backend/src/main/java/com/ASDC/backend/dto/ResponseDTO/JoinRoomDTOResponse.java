package com.ASDC.backend.dto.ResponseDTO;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JoinRoomDTOResponse {

    private UserDTOResponse userDTOResponse;
    private LocalDateTime join_date;
    private RoomDTOResponse roomDTOResponse;
}
