package com.ASDC.backend.controller;

import com.ASDC.backend.dto.RequestDTO.AnnouncementRequestDTO;
import com.ASDC.backend.dto.ResponseDTO.AnnouncementDTOResponse;
import com.ASDC.backend.entity.Announcement;
import com.ASDC.backend.mapper.AnnouncementMapper;
import com.ASDC.backend.service.Interface.AnnouncementService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for handling announcement-related requests.
 */
@RestController
@RequestMapping("/api/v1/announcements")
@CrossOrigin
public class AnnouncementController {

    private final AnnouncementService announcementService;
    private final AnnouncementMapper announcementMapper;
    private static final Logger logger = LoggerFactory.getLogger(AnnouncementController.class);

    public AnnouncementController(AnnouncementService announcementService, AnnouncementMapper announcementMapper) {
        this.announcementService = announcementService;
        this.announcementMapper = announcementMapper;
    }

    /**
     * Get all announcements.
     *
     * @return List of AnnouncementDTOResponse
     */
    @GetMapping("/get/all")
    public List<AnnouncementDTOResponse> getAllAnnouncements() {
        logger.info("Fetching all announcements");
        return announcementService.getAllAnnouncements();
    }

    /**
     * Get an announcement by its ID.
     *
     * @param id the ID of the announcement
     * @return AnnouncementDTOResponse
     */
    @GetMapping("/get/{id}")
    public AnnouncementDTOResponse getAnnouncementById(@PathVariable Long id) {
        logger.info("Fetching announcement with ID: {}", id);
        Announcement announcement = announcementService.getAnnouncementById(id);
        return announcementMapper.toDTO(announcement);
    }

    /**
     * Create a new announcement.
     *
     * @param announcementRequestDTO the request DTO for creating an announcement
     * @return ResponseEntity containing the created AnnouncementDTOResponse
     */
    @PostMapping("/add")
    public ResponseEntity<AnnouncementDTOResponse> createAnnouncement(@RequestBody AnnouncementRequestDTO announcementRequestDTO) {
        logger.info("Creating new announcement with title: {}", announcementRequestDTO.getTitle());
        return ResponseEntity.ok(announcementService.addAnnouncement(announcementRequestDTO));
    }

    /**
     * Update an existing announcement.
     * @param announcementRequestDTO the request DTO for updating an announcement
     * @param id the ID of the announcement to update
     * @return ResponseEntity containing the updated AnnouncementDTOResponse
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<AnnouncementDTOResponse> updateAnnouncement(@RequestBody AnnouncementRequestDTO announcementRequestDTO, @PathVariable Long id) {
        logger.info("Updating announcement with ID: {}", id);
        return ResponseEntity.ok(announcementService.updateAnnouncement(announcementRequestDTO, id));
    }

    /**
     * Delete an announcement by its ID.
     * @param id the ID of the announcement to delete2
     * @return ResponseEntity containing a success message
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteAnnouncement(@PathVariable Long id) {
        logger.info("Deleting announcement with ID: {}", id);
        announcementService.deleteAnnouncement(id);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }
}