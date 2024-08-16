package com.ASDC.backend.dto.RequestDTO;

import lombok.*;

import java.time.LocalDate;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTORequest {

    private String name;
    private LocalDate taskDate;
    private String description;
    private String assignedTO;

}
