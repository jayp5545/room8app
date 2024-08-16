package com.ASDC.backend.dto.ResponseDTO;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseDTOResponse {

    private int id;
    private RoomDTOResponse room;
    private String description;
    private double amount;
    private UserDTOResponse paidBy;
    private List<ParticipantDTOResponse> participants;
    private boolean status;
    private LocalDateTime dateOfCreation;
    private LocalDateTime lastModifiedOn;
    private UserDTOResponse createdBy;
    private UserDTOResponse lastModifiedBy;
}