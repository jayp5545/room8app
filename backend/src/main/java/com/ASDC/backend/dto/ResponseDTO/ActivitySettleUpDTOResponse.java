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
public class ActivitySettleUpDTOResponse {

    private int id;
    private LocalDateTime date;
    private String details;
    private Activity.ActivityType type;
    private SettleUpDTOResponse settleup;
    private RoomDTOResponse room;
}
