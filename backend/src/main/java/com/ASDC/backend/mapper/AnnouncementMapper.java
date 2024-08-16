package com.ASDC.backend.mapper;

import com.ASDC.backend.dto.ResponseDTO.AnnouncementDTOResponse;
import com.ASDC.backend.entity.Announcement;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AnnouncementMapper {

    public AnnouncementDTOResponse toDTO(Announcement announcement) {
        if (announcement == null) {
            return null;
        }

        return AnnouncementDTOResponse.builder()
                .id(announcement.getId())
                .title(announcement.getTitle())
                .description(announcement.getDescription())
                .postedDateTime(announcement.getPostedDateTime())
                .lastUpdatedDateTime(announcement.getLastUpdatedDateTime())
                .userPostedFirstName(announcement.getUserPosted() != null ? announcement.getUserPosted().getFirstName() : null)
                .userPostedLastName(announcement.getUserPosted() != null ? announcement.getUserPosted().getLastName() : null)
                .userModifiedFirstName(announcement.getUserModified() != null ? announcement.getUserModified().getFirstName() : null)
                .userModifiedLastName(announcement.getUserModified() != null ? announcement.getUserModified().getLastName() : null)
                .build();
    }

    public List<AnnouncementDTOResponse> toDTOList(List<Announcement> announcements) {
        List<AnnouncementDTOResponse> announcementDTOResponses = new ArrayList<>();
        for (Announcement announcement : announcements) {
            announcementDTOResponses.add(toDTO(announcement));
        }
        return announcementDTOResponses;
    }
}