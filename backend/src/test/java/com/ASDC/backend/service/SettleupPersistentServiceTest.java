package com.ASDC.backend.service;

import com.ASDC.backend.DummyData.DummyData;
import com.ASDC.backend.Models.SettleUpAmountDTOProjection;
import com.ASDC.backend.entity.SettleUp;
import com.ASDC.backend.exception.CustomException;
import com.ASDC.backend.repository.SettleUpRepository;
import com.ASDC.backend.service.PersistentServices.SettleupPersistentService;
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
public class SettleupPersistentServiceTest {

    @Mock
    private SettleUpRepository settleUpRepository;

    @InjectMocks
    private SettleupPersistentService settleupPersistentService;

    private SettleUp settleUp;
    private SettleUpAmountDTOProjection settleUpAmountDTOProjection;
    private DummyData dummyData;

    @BeforeEach
    void setUp() {
        dummyData = new DummyData();
        settleUp = dummyData.createSettleUp();
        settleUpAmountDTOProjection = mock(SettleUpAmountDTOProjection.class);
    }

    @Test
    public void saveSettleUp_Success() {
        when(settleUpRepository.save(settleUp)).thenReturn(settleUp);

        SettleUp result = settleupPersistentService.saveSettleUp(settleUp);

        assertNotNull(result);
        assertEquals(settleUp.getId(), result.getId());
        verify(settleUpRepository, times(1)).save(settleUp);
    }

    @Test
    public void getSettleUp_Found() {
        when(settleUpRepository.findById(settleUp.getId())).thenReturn(Optional.of(settleUp));

        SettleUp result = settleupPersistentService.getSettleUp(settleUp.getId());

        assertNotNull(result);
        assertEquals(settleUp.getId(), result.getId());
        verify(settleUpRepository, times(1)).findById(settleUp.getId());
    }

    @Test
    public void getSettleUp_NotFound() {
        when(settleUpRepository.findById(settleUp.getId())).thenReturn(Optional.empty());

        CustomException thrown = assertThrows(CustomException.class, () ->
                settleupPersistentService.getSettleUp(settleUp.getId()));

        assertEquals("400", thrown.getErrorCode());
        assertEquals("Settle up with ID " + settleUp.getId() + " does not exist.", thrown.getErrorMessage());
        verify(settleUpRepository, times(1)).findById(settleUp.getId());
    }

    @Test
    public void getAllSettleUpActive_Found() {
        when(settleUpRepository.findAllByRoomActive(settleUp.getRoom().getId())).thenReturn(Arrays.asList(settleUp));

        List<SettleUp> result = settleupPersistentService.getAllSettleUpActive(settleUp.getRoom().getId());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(settleUp.getId(), result.get(0).getId());
        verify(settleUpRepository, times(1)).findAllByRoomActive(settleUp.getRoom().getId());
    }

    @Test
    public void getAllSettleUpInActive_Found() {
        when(settleUpRepository.findAllByRoomInActive(settleUp.getRoom().getId())).thenReturn(Arrays.asList(settleUp));

        List<SettleUp> result = settleupPersistentService.getAllSettleUpInActive(settleUp.getRoom().getId());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(settleUp.getId(), result.get(0).getId());
        verify(settleUpRepository, times(1)).findAllByRoomInActive(settleUp.getRoom().getId());
    }

    @Test
    public void getAllSettleUp_Found() {
        when(settleUpRepository.findAllByRoom(settleUp.getRoom().getId())).thenReturn(Arrays.asList(settleUp));

        List<SettleUp> result = settleupPersistentService.getAllSettleUp(settleUp.getRoom().getId());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(settleUp.getId(), result.get(0).getId());
        verify(settleUpRepository, times(1)).findAllByRoom(settleUp.getRoom().getId());
    }

    @Test
    public void findSettleUpBySum_Found() {
        when(settleUpRepository.findBySum(settleUp.getRoom().getId())).thenReturn(Arrays.asList(settleUpAmountDTOProjection));

        List<SettleUpAmountDTOProjection> result = settleupPersistentService.findSettleUpBySum(settleUp.getRoom().getId());

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(settleUpRepository, times(1)).findBySum(settleUp.getRoom().getId());
    }
}