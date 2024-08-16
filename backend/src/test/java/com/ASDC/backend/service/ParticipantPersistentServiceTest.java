package com.ASDC.backend.service;

import com.ASDC.backend.DummyData.DummyData;
import com.ASDC.backend.entity.Participant;
import com.ASDC.backend.exception.CustomException;
import com.ASDC.backend.repository.ParticipantRepository;
import com.ASDC.backend.service.PersistentServices.ParticipantPersistentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ParticipantPersistentServiceTest {

    @Mock
    private ParticipantRepository participantRepository;

    @InjectMocks
    private ParticipantPersistentService participantPersistentService;

    private Participant participant1;
    private Participant participant2;
    private DummyData dummyData;

    @BeforeEach
    void setUp() {
        dummyData = new DummyData();
        participant1 = dummyData.createParticipant();
        participant1.setId(1);

        participant2 = dummyData.createParticipant();
        participant2.setId(2);
    }

    @Test
    public void saveParticipants_Success() {
        List<Participant> participants = Arrays.asList(participant1, participant2);
        when(participantRepository.saveAll(participants)).thenReturn(participants);

        List<Participant> result = participantPersistentService.saveParticipants(participants);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(participant1.getId(), result.get(0).getId());
        assertEquals(participant2.getId(), result.get(1).getId());
        verify(participantRepository, times(1)).saveAll(participants);
    }

    @Test
    public void deleteParticipants_Success() {
        List<Participant> participants = Arrays.asList(participant1, participant2);
        when(participantRepository.existsById(participant1.getId())).thenReturn(true);
        when(participantRepository.existsById(participant2.getId())).thenReturn(true);

        participantPersistentService.deleteParticipants(participants);

        verify(participantRepository, times(1)).deleteById(participant1.getId());
        verify(participantRepository, times(1)).deleteById(participant2.getId());
    }

    @Test
    public void deleteParticipants_ParticipantNotFound() {
        List<Participant> participants = Arrays.asList(participant1, participant2);
        when(participantRepository.existsById(participant1.getId())).thenReturn(true);
        when(participantRepository.existsById(participant2.getId())).thenReturn(false);

        CustomException thrown = assertThrows(CustomException.class, () ->
                participantPersistentService.deleteParticipants(participants));

        assertEquals("400", thrown.getErrorCode());
        assertEquals("Participant with ID " + participant2.getId() + " does not exist.", thrown.getErrorMessage());
        verify(participantRepository, times(1)).deleteById(participant1.getId());
        verify(participantRepository, times(0)).deleteById(participant2.getId());
    }
}