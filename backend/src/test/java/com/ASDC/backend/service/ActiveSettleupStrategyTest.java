package com.ASDC.backend.service;

import com.ASDC.backend.DummyData.DummyData;
import com.ASDC.backend.entity.Room;
import com.ASDC.backend.entity.SettleUp;
import com.ASDC.backend.service.PersistentServices.SettleupPersistentService;
import com.ASDC.backend.service.StrategyFactory.ActiveSettleupStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ActiveSettleupStrategyTest {

    @Mock
    private SettleupPersistentService settleupPersistentService;

    @InjectMocks
    private ActiveSettleupStrategy activeSettleupStrategy;

    private Room room;
    private DummyData dummyData;

    @BeforeEach
    public void setUp() {
        dummyData = new DummyData();
        room = dummyData.createRoom();
    }

    @Test
    public void retrieveItems_ReturnsActiveSettleUps() {
        List<SettleUp> settleUps = List.of(new SettleUp(), new SettleUp());
        when(settleupPersistentService.getAllSettleUpActive(room.getId())).thenReturn(settleUps);

        List<SettleUp> result = activeSettleupStrategy.retrieveItems(room);
        assertNotNull(result);
        assertEquals(2, result.size());
    }
}