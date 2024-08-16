package com.ASDC.backend.service.Interface;

import com.ASDC.backend.dto.RequestDTO.AnnouncementRequestDTO;
import com.ASDC.backend.dto.ResponseDTO.AnnouncementDTOResponse;
import com.ASDC.backend.entity.Announcement;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AnnouncementService {

    List<AnnouncementDTOResponse> getAllAnnouncements();

    AnnouncementDTOResponse addAnnouncement(AnnouncementRequestDTO announcementRequestDTO);

    Announcement getAnnouncementById(Long id);

    AnnouncementDTOResponse updateAnnouncement(AnnouncementRequestDTO announcementRequestDTO, Long id);

    boolean checkIfAnnouncementExists(String title);

    void deleteAnnouncement(Long id);
}

