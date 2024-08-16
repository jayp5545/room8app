package com.ASDC.backend.dto.RequestDTO;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomDTORequest {

    private int id;

    @NotBlank(message = "Room name can't be null or empty!")
    private String name;
}
