package com.ASDC.backend.dto.RequestDTO;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JoinRoomDTORequest {

    @Min(value = 5, message = "Join code must be of 5 digits!")
    @Max(value = 5, message = "Join code must be of 5 digits!")
    private int joinCode;

}
