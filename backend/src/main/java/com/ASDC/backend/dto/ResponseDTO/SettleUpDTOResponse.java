package com.ASDC.backend.dto.ResponseDTO;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SettleUpDTOResponse {

    private int id;
    private RoomDTOResponse room;
    private UserDTOResponse paidBy;
    private UserDTOResponse paidTo;
    private double amount;
    private boolean status;
    private LocalDateTime dateOfCreation;
    private LocalDateTime lastModifiedOn;
    private UserDTOResponse createdBy;
    private UserDTOResponse lastModifiedBy;
}
