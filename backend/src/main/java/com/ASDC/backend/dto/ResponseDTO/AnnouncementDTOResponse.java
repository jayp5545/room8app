package com.ASDC.backend.dto.ResponseDTO;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnnouncementDTOResponse {
    private Long id;
    private String title;
    private String description;
    private String userPostedFirstName;
    private String userPostedLastName;
    private String userModifiedFirstName;
    private String userModifiedLastName;
    private LocalDateTime postedDateTime;;
    private LocalDateTime lastUpdatedDateTime;
}

