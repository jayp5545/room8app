package com.ASDC.backend.repository;


import com.ASDC.backend.Models.SettleUpAmountDTOProjection;
import com.ASDC.backend.dto.ResponseDTO.SettleUpAmountDTOResponse;
import com.ASDC.backend.entity.SettleUp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SettleUpRepository extends JpaRepository<SettleUp, Integer> {

    @Query(value = "SELECT * FROM settleup where room_id = :roomID AND status = '1'", nativeQuery = true)
    List<SettleUp> findAllByRoomActive(@Param("roomID") int roomID);

    @Query(value = "SELECT * FROM settleup where room_id = :roomID AND status = '0'", nativeQuery = true)
    List<SettleUp> findAllByRoomInActive(@Param("roomID") int roomID);

    @Query(value = "SELECT * FROM settleup where room_id = :roomID", nativeQuery = true)
    List<SettleUp> findAllByRoom(@Param("roomID") int roomID);

    @Query(value = "SELECT paid_by, paid_to, SUM(amount) AS amount, status FROM settleup WHERE room_id = :roomID AND status = '1' GROUP BY paid_by, paid_to", nativeQuery = true)
    List<SettleUpAmountDTOProjection> findBySum(@Param("roomID") int roomID);
}
