package com.ASDC.backend.dto.ResponseDTO;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantDTOResponse {

    private int id;
    private UserDTOResponse user;
    private double amount;
}
