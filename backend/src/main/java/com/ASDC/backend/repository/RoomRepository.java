package com.ASDC.backend.repository;

import com.ASDC.backend.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {
    Optional<Room> findByName(String roomName);
    Optional<Room> findByCode(int code);

    @Query(value = "SELECT * FROM room where id = :ID", nativeQuery = true)
    Optional<Room> findById(int ID);
}
