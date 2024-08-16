package com.ASDC.backend.dto.RequestDTO;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnnouncementRequestDTO {
    private String title;
    private String description;
}
