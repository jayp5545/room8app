package com.ASDC.backend.dto.ResponseDTO;

import com.ASDC.backend.dto.UserDTO;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTOResponse {

    private int id;
    private RoomDTOResponse room;
    private String name;
    private LocalDateTime date_of_creation;
    private LocalDateTime last_modified_On;
    private LocalDate taskDate;
    private UserDTOResponse created_by;
    private UserDTOResponse last_modified_by;
    private String description;
    private UserDTOResponse assignedTo;
    private boolean finished;
}
