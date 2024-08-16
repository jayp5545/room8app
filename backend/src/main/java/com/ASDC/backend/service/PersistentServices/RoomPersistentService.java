package com.ASDC.backend.service.PersistentServices;

import com.ASDC.backend.entity.Room;
import com.ASDC.backend.exception.CustomException;
import com.ASDC.backend.repository.RoomRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class RoomPersistentService {

    private final RoomRepository roomRepository;

    public RoomPersistentService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public Room saveRoom(Room room) {
        return roomRepository.save(room);
    }

    public Optional<Room> getRoomById(int id) {
        return roomRepository.findById(id);
    }

    public Optional<Room> getRoomByName(String roomName) {
        return roomRepository.findByName(roomName);
    }

    public void deleteRoomById(int id) {
        roomRepository.deleteById(id);
    }

    public Optional<Room> getRoomByCode(int code) {
        return roomRepository.findByCode(code);
    }

}
