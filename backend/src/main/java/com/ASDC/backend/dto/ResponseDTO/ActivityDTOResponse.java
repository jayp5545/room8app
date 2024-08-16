package com.ASDC.backend.dto.ResponseDTO;

import com.ASDC.backend.entity.Activity;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityDTOResponse {

    private int id;
    private Activity.ActivityType type;
    private ExpenseDTOResponse expense;
    private SettleUpDTOResponse settleUp;
    private LocalDateTime date;
    private String details;
    private RoomDTOResponse room;
}
