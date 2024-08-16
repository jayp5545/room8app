package com.ASDC.backend.dto.ResponseDTO;

import com.ASDC.backend.entity.Activity;
import com.ASDC.backend.entity.SettleUp;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityExpenseDTOResponse {

    private int id;
    private LocalDateTime date;
    private String details;
    private Activity.ActivityType type;
    private ExpenseDTOResponse expense;
    private RoomDTOResponse room;
}
