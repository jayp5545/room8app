    package com.ASDC.backend.dto;

    import com.ASDC.backend.dto.ResponseDTO.RoomDTOResponse;
    import lombok.*;

    import java.time.LocalDateTime;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public class AnnouncementDTO {
        private int id;
        private RoomDTOResponse room;
        private String title;
        private String description;
        private LocalDateTime date_of_creation;
        private LocalDateTime last_modified_On;
        private UserDTO created_by;
        private UserDTO last_modified_by;
        private boolean isActive;
    }
