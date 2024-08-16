package com.ASDC.backend.dto.ResponseDTO;

import lombok.*;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomDTOResponse {

    private int id;
    private String name;
    private int members;
    private int code;
    private boolean active;
}
