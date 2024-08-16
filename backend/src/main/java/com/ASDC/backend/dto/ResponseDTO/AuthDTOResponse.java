package com.ASDC.backend.dto.ResponseDTO;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthDTOResponse {
    private RoomDTOResponse roomDTOResponse;
    private String token;
    private String email;
}

