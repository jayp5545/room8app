package com.ASDC.backend.service.implementation;

import com.ASDC.backend.dto.RequestDTO.AnnouncementRequestDTO;
import com.ASDC.backend.dto.ResponseDTO.AnnouncementDTOResponse;
import com.ASDC.backend.entity.Announcement;
import com.ASDC.backend.entity.Room;
import com.ASDC.backend.entity.User;
import com.ASDC.backend.entity.UserRoomMapping;
import com.ASDC.backend.exception.CustomException;
import com.ASDC.backend.mapper.AnnouncementMapper;
import com.ASDC.backend.repository.AnnouncementRepository;
import com.ASDC.backend.service.Interface.AnnouncementService;
import com.ASDC.backend.service.PersistentServices.AnnouncementPersistentService;
import com.ASDC.backend.util.UtilFunctions;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of the AnnouncementService interface.
 */
@Service
public class AnnouncementServiceImpl implements AnnouncementService {

    private final AnnouncementRepository announcementRepository;
    private final UtilFunctions utilFunctions;
    private final AnnouncementMapper announcementMapper;
    private final AnnouncementPersistentService announcementPersistentService;
    private static final Logger logger = LoggerFactory.getLogger(AnnouncementServiceImpl.class);

    public AnnouncementServiceImpl(AnnouncementRepository announcementRepository, UtilFunctions utilFunctions, AnnouncementMapper announcementMapper, AnnouncementPersistentService announcementPersistentService) {
        this.announcementRepository = announcementRepository;
        this.utilFunctions = utilFunctions;
        this.announcementMapper = announcementMapper;
        this.announcementPersistentService = announcementPersistentService;
    }

    /**
     * Get all announcements for the current user's room.
     * @return List of AnnouncementDTOResponse
     */
    @Transactional
    @Override
    public List<AnnouncementDTOResponse> getAllAnnouncements() {
        logger.info("Fetching all announcements for the current user's room");
        User user = utilFunctions.getCurrentUser();
        UserRoomMapping userRoomMapping = utilFunctions.getUserRoomMapping(user);
        List<Announcement> announcements = announcementPersistentService.findAllAnnouncementsByRoom(userRoomMapping.getRoomid().getId());
        // Convert to DTO list
        return announcementMapper.toDTOList(announcements);
    }

    /**
     * Add a new announcement.
     * @param announcementRequestDTO the request DTO for creating an announcement
     * @return AnnouncementDTOResponse
     */
    @Override
    public AnnouncementDTOResponse addAnnouncement(AnnouncementRequestDTO announcementRequestDTO) {
        logger.info("Adding new announcement with title: {}", announcementRequestDTO.getTitle());
        User user = utilFunctions.getCurrentUser();
        UserRoomMapping userRoomMapping = utilFunctions.getUserRoomMapping(user);
        Room room = utilFunctions.getRoom(userRoomMapping);

        validateAnnouncementRequest(announcementRequestDTO);
        if (checkIfAnnouncementExists(announcementRequestDTO.getTitle())) {
            logger.warn("Announcement with title '{}' already exists", announcementRequestDTO.getTitle());
            throw new CustomException("400", "Announcement with this title already exists");
        }

        Announcement announcement = Announcement.builder()
                .title(announcementRequestDTO.getTitle())
                .description(announcementRequestDTO.getDescription())
                .userPosted(user)
                .postedDateTime(LocalDateTime.now())
                .isActive(true)
                .room(room)
                .build();

        Announcement savedAnnouncement = announcementPersistentService.saveAnnouncement(announcement);
        return announcementMapper.toDTO(savedAnnouncement);
    }

    /**
     * Get an announcement by its ID.
     * @param id the ID of the announcement
     * @return Announcement
     */
    @Override
    public Announcement getAnnouncementById(Long id) {
        logger.info("Fetching announcement with ID: {}", id);
        Optional<Announcement> announcementOptional = announcementPersistentService.findById(id);
        return announcementOptional.orElse(null);
    }

    /**
     * Update an existing announcement.
     * @param announcementRequestDTO the request DTO for updating an announcement
     * @param id the ID of the announcement to update
     * @return AnnouncementDTOResponse
     */
    @Override
    public AnnouncementDTOResponse updateAnnouncement(AnnouncementRequestDTO announcementRequestDTO, Long id) {
        logger.info("Updating announcement with ID: {}", id);
        validateAnnouncementRequest(announcementRequestDTO);
        Announcement existingAnnouncement = getAnnouncementById(id);
        if (existingAnnouncement == null) {
            throw new CustomException("404", "Announcement not found");
        }
        boolean isTitleChanged = !existingAnnouncement.getTitle().equals(announcementRequestDTO.getTitle());

        // If announcement title changes and matches same title of other.
        if (isTitleChanged && checkIfAnnouncementExists(announcementRequestDTO.getTitle())) {
            throw new CustomException("400", "Announcement with this title already exists");
        }
        User currentUser = utilFunctions.getCurrentUser();
        // Update fields
        Announcement updatedAnnouncement = updateAnnouncementProperties(announcementRequestDTO, currentUser, existingAnnouncement);
        announcementPersistentService.saveAnnouncement(updatedAnnouncement);
        return announcementMapper.toDTO(updatedAnnouncement);
    }

    /**
     * Validate the announcement request.
     * @param announcementRequestDTO the request DTO for creating/updating an announcement
     */
    private void validateAnnouncementRequest(AnnouncementRequestDTO announcementRequestDTO) {
        if (announcementRequestDTO.getTitle() == null) {
            throw new CustomException("400", "Announcement Name can't be Null!");
        }
        if (announcementRequestDTO.getTitle().isBlank()) {
            throw new CustomException("400", "Announcement Name can't be Empty!");
        }
        if (announcementRequestDTO.getDescription() == null) {
            throw new CustomException("400", "Announcement description can't be Null!");
        }
        if (announcementRequestDTO.getDescription().isBlank()) {
            throw new CustomException("400", "Announcement description can't be Empty!");
        }
    }

    /**
     * Update the properties of an existing announcement.
     * @param announcementRequestDTO the request DTO for updating an announcement
     * @param user the user making the update
     * @param existingAnnouncement the existing announcement to update
     * @return updated Announcement
     */
    public Announcement updateAnnouncementProperties(AnnouncementRequestDTO announcementRequestDTO, User user, Announcement existingAnnouncement) {
        existingAnnouncement.setTitle(announcementRequestDTO.getTitle());
        existingAnnouncement.setDescription(announcementRequestDTO.getDescription());
        existingAnnouncement.setLastUpdatedDateTime(LocalDateTime.now());
        existingAnnouncement.setUserModified(user);
        return existingAnnouncement;
    }

    /**
     * Check if an announcement with the given title already exists.
     *
     * @param title the title of the announcement
     * @return true if the announcement exists, false otherwise
     */
    public boolean checkIfAnnouncementExists(String title) {
        Optional<Announcement> announcementOptional = announcementPersistentService.findByTitle(title);
        return announcementOptional.isPresent();
    }

    /**
     * Delete an announcement by its ID.
     * @param id the ID of the announcement to delete
     */
    public void deleteAnnouncement(Long id) {
        logger.info("Deleting announcement with ID: {}", id);
        if (!announcementPersistentService.existsById(id)) {
            throw new CustomException("404", "Announcement not found");
        }

        Announcement announcement = getAnnouncementById(id);
        announcementPersistentService.deleteAnnouncement(announcement);
    }
}