package com.ASDC.backend.service.PersistentServices;

import com.ASDC.backend.Models.SettleUpAmountDTOProjection;
import com.ASDC.backend.entity.SettleUp;
import com.ASDC.backend.exception.CustomException;
import com.ASDC.backend.repository.SettleUpRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class SettleupPersistentService {

    private final SettleUpRepository settleUpRepository;

    @Autowired
    public SettleupPersistentService(SettleUpRepository settleUpRepository) {
        this.settleUpRepository = settleUpRepository;
    }

    public SettleUp saveSettleUp(SettleUp settleUp) {
        return settleUpRepository.save(settleUp);
    }

    public SettleUp getSettleUp(int id) {
        return settleUpRepository.findById(id)
                .orElseThrow(() -> new CustomException("400", "Settle up with ID " + id + " does not exist."));
    }

    public List<SettleUp> getAllSettleUpActive(int roomId) {
        return settleUpRepository.findAllByRoomActive(roomId);
    }

    public List<SettleUp> getAllSettleUpInActive(int roomId) {
        return settleUpRepository.findAllByRoomInActive(roomId);
    }

    public List<SettleUp> getAllSettleUp(int roomId) {
        return settleUpRepository.findAllByRoom(roomId);
    }

    public List<SettleUpAmountDTOProjection> findSettleUpBySum(int roomId) {
        return settleUpRepository.findBySum(roomId);
    }
}
