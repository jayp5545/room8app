package com.ASDC.backend.repository;

import com.ASDC.backend.entity.Task;
import com.ASDC.backend.entity.UserRoomMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRoomRepository extends JpaRepository<UserRoomMapping, Integer> {

    @Query(value = "SELECT * FROM user_room where user_id = :user_id", nativeQuery = true)
    Optional<UserRoomMapping> findByUserID(@Param("user_id") Long user_id);

    @Query(value = "SELECT * FROM user_room where room_id = :room_id", nativeQuery = true)
    Optional<List<UserRoomMapping>> findAllByRoom(int room_id);
}
