package com.ASDC.backend.service;

import com.ASDC.backend.DummyData.DummyData;
import com.ASDC.backend.dto.RequestDTO.AnnouncementRequestDTO;
import com.ASDC.backend.dto.ResponseDTO.AnnouncementDTOResponse;
import com.ASDC.backend.entity.Announcement;
import com.ASDC.backend.entity.Room;
import com.ASDC.backend.entity.User;
import com.ASDC.backend.entity.UserRoomMapping;
import com.ASDC.backend.exception.CustomException;
import com.ASDC.backend.mapper.AnnouncementMapper;
import com.ASDC.backend.service.PersistentServices.AnnouncementPersistentService;
import com.ASDC.backend.service.implementation.AnnouncementServiceImpl;
import com.ASDC.backend.util.UtilFunctions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AnnouncementServiceImplTest {

    @Mock
    private AnnouncementPersistentService announcementPersistentService;

    @Mock
    private UtilFunctions utilFunctions;

    @Mock
    private AnnouncementMapper announcementMapper;

    @InjectMocks
    private AnnouncementServiceImpl announcementService;

    private User user;
    private Room room;
    private UserRoomMapping userRoomMapping;
    private Announcement announcement;
    private AnnouncementRequestDTO announcementRequestDTO;
    private AnnouncementDTOResponse announcementDTOResponse;
    private DummyData dummyData;

    @BeforeEach
    void setUp() {
        dummyData = new DummyData();
        user = dummyData.createUserForUserService();
        room = dummyData.createRoom();
        userRoomMapping = dummyData.createUserRoomMapping();
        announcement = dummyData.createAnnouncement(user, room);
        announcementRequestDTO = dummyData.createAnnouncementRequestDTO();
        announcementDTOResponse = dummyData.createAnnouncementDTOResponse();
    }

    @Test
    void getAllAnnouncements_Success() {
        when(utilFunctions.getCurrentUser()).thenReturn(user);
        when(utilFunctions.getUserRoomMapping(user)).thenReturn(userRoomMapping);
        when(announcementPersistentService.findAllAnnouncementsByRoom(room.getId())).thenReturn(Arrays.asList(announcement));
        when(announcementMapper.toDTOList(any())).thenReturn(Arrays.asList(announcementDTOResponse));

        List<AnnouncementDTOResponse> result = announcementService.getAllAnnouncements();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(announcementDTOResponse, result.get(0));
        verify(announcementPersistentService, times(1)).findAllAnnouncementsByRoom(room.getId());
    }

    @Test
    void addAnnouncement_Success() {
        when(utilFunctions.getCurrentUser()).thenReturn(user);
        when(utilFunctions.getUserRoomMapping(user)).thenReturn(userRoomMapping);
        when(utilFunctions.getRoom(userRoomMapping)).thenReturn(room);
        when(announcementPersistentService.saveAnnouncement(any(Announcement.class))).thenReturn(announcement);
        when(announcementMapper.toDTO(any(Announcement.class))).thenReturn(announcementDTOResponse);

        AnnouncementDTOResponse result = announcementService.addAnnouncement(announcementRequestDTO);

        assertNotNull(result);
        assertEquals(announcementDTOResponse, result);
        verify(announcementPersistentService, times(1)).saveAnnouncement(any(Announcement.class));
    }

    @Test
    void addAnnouncement_TitleAlreadyExists() {
        when(utilFunctions.getCurrentUser()).thenReturn(user);
        when(utilFunctions.getUserRoomMapping(user)).thenReturn(userRoomMapping);
        when(utilFunctions.getRoom(userRoomMapping)).thenReturn(room);
        when(announcementPersistentService.findByTitle(announcementRequestDTO.getTitle())).thenReturn(Optional.of(announcement));

        CustomException exception = assertThrows(CustomException.class, () -> {
            announcementService.addAnnouncement(announcementRequestDTO);
        });

        assertEquals("400", exception.getErrorCode());
        assertEquals("Announcement with this title already exists", exception.getErrorMessage());
    }

    @Test
    void updateAnnouncement_Success() {
        when(announcementPersistentService.findById(1L)).thenReturn(Optional.of(announcement));
        when(utilFunctions.getCurrentUser()).thenReturn(user);
        when(announcementPersistentService.saveAnnouncement(any(Announcement.class))).thenReturn(announcement);
        when(announcementMapper.toDTO(any(Announcement.class))).thenReturn(announcementDTOResponse);

        AnnouncementDTOResponse result = announcementService.updateAnnouncement(announcementRequestDTO, 1L);

        assertNotNull(result);
        assertEquals(announcementDTOResponse, result);
        verify(announcementPersistentService, times(1)).saveAnnouncement(any(Announcement.class));
    }

    @Test
    void updateAnnouncement_AnnouncementNotFound() {
        when(announcementPersistentService.findById(1L)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> {
            announcementService.updateAnnouncement(announcementRequestDTO, 1L);
        });

        assertEquals("404", exception.getErrorCode());
        assertEquals("Announcement not found", exception.getErrorMessage());
    }

    @Test
    void deleteAnnouncement_Success() {
        when(announcementPersistentService.existsById(1L)).thenReturn(true);
        when(announcementPersistentService.findById(1L)).thenReturn(Optional.of(announcement));

        assertDoesNotThrow(() -> announcementService.deleteAnnouncement(1L));

        verify(announcementPersistentService, times(1)).deleteAnnouncement(announcement);
    }

    @Test
    void deleteAnnouncement_AnnouncementNotFound() {
        when(announcementPersistentService.existsById(1L)).thenReturn(false);

        CustomException exception = assertThrows(CustomException.class, () -> {
            announcementService.deleteAnnouncement(1L);
        });

        assertEquals("404", exception.getErrorCode());
        assertEquals("Announcement not found", exception.getErrorMessage());
    }
}