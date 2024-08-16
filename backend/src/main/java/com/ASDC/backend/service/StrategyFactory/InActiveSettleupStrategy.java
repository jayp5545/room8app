package com.ASDC.backend.service.StrategyFactory;

import com.ASDC.backend.entity.Room;
import com.ASDC.backend.entity.SettleUp;
import com.ASDC.backend.service.Interface.RetrievalStrategy;
import com.ASDC.backend.service.PersistentServices.SettleupPersistentService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InActiveSettleupStrategy implements RetrievalStrategy<SettleUp> {

    private final SettleupPersistentService settleupPersistentService;

    public InActiveSettleupStrategy(SettleupPersistentService settleupPersistentService) {
        this.settleupPersistentService = settleupPersistentService;
    }

    @Override
    public List<SettleUp> retrieveItems(Room room) {
        return settleupPersistentService.getAllSettleUpInActive(room.getId());
    }
}
