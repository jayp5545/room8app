package com.ASDC.backend.service;

import com.ASDC.backend.DummyData.DummyData;
import com.ASDC.backend.entity.Announcement;
import com.ASDC.backend.repository.AnnouncementRepository;
import com.ASDC.backend.service.PersistentServices.AnnouncementPersistentService;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AnnouncementPersistentServiceTest {

    @Mock
    private AnnouncementRepository announcementRepository;

    @InjectMocks
    private AnnouncementPersistentService announcementPersistentService;

    private DummyData dummyData;
    private Announcement announcement;

    @BeforeEach
    void setUp() {
        dummyData = new DummyData();
        announcement = dummyData.createAnnouncement(dummyData.createUser(), dummyData.createRoom());
    }

    @Test
    void findAllAnnouncementsByRoom_Success() {
        int roomId = 1;
        when(announcementRepository.findAllAnnouncementsByRoom(roomId)).thenReturn(Arrays.asList(announcement));

        List<Announcement> result = announcementPersistentService.findAllAnnouncementsByRoom(roomId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(announcement, result.get(0));
        verify(announcementRepository, times(1)).findAllAnnouncementsByRoom(roomId);
    }

    @Test
    void saveAnnouncement_Success() {
        when(announcementRepository.save(any(Announcement.class))).thenReturn(announcement);

        Announcement result = announcementPersistentService.saveAnnouncement(announcement);

        assertNotNull(result);
        assertEquals(announcement, result);
        verify(announcementRepository, times(1)).save(announcement);
    }

    @Test
    void findById_Success() {
        Long id = 1L;
        when(announcementRepository.findById(id)).thenReturn(Optional.of(announcement));

        Optional<Announcement> result = announcementPersistentService.findById(id);

        assertTrue(result.isPresent());
        assertEquals(announcement, result.get());
        verify(announcementRepository, times(1)).findById(id);
    }

    @Test
    void findById_NotFound() {
        Long id = 1L;
        when(announcementRepository.findById(id)).thenReturn(Optional.empty());

        Optional<Announcement> result = announcementPersistentService.findById(id);

        assertFalse(result.isPresent());
        verify(announcementRepository, times(1)).findById(id);
    }

    @Test
    void findByTitle_Success() {
        String title = "Test Announcement";
        when(announcementRepository.findByTitle(title)).thenReturn(Optional.of(announcement));

        Optional<Announcement> result = announcementPersistentService.findByTitle(title);

        assertTrue(result.isPresent());
        assertEquals(announcement, result.get());
        verify(announcementRepository, times(1)).findByTitle(title);
    }

    @Test
    void findByTitle_NotFound() {
        String title = "Test Announcement";
        when(announcementRepository.findByTitle(title)).thenReturn(Optional.empty());

        Optional<Announcement> result = announcementPersistentService.findByTitle(title);

        assertFalse(result.isPresent());
        verify(announcementRepository, times(1)).findByTitle(title);
    }

    @Test
    void existsById_Success() {
        Long id = 1L;
        when(announcementRepository.existsById(id)).thenReturn(true);

        boolean result = announcementPersistentService.existsById(id);

        assertTrue(result);
        verify(announcementRepository, times(1)).existsById(id);
    }

    @Test
    void existsById_NotFound() {
        Long id = 1L;
        when(announcementRepository.existsById(id)).thenReturn(false);

        boolean result = announcementPersistentService.existsById(id);

        assertFalse(result);
        verify(announcementRepository, times(1)).existsById(id);
    }

    @Test
    void deleteAnnouncement_Success() {
        doNothing().when(announcementRepository).delete(any(Announcement.class));

        announcementPersistentService.deleteAnnouncement(announcement);

        verify(announcementRepository, times(1)).delete(announcement);
    }
}
