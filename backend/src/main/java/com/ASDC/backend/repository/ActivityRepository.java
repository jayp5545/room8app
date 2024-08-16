package com.ASDC.backend.repository;

import com.ASDC.backend.entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Integer> {

    @Query(value = "SELECT * FROM activity where settleup_id IS NULL AND room_id = :roomID", nativeQuery = true)
    List<Activity> findAllForExpense(@Param("roomID") int roomID);

    @Query(value = "SELECT * FROM activity where room_id = :roomID", nativeQuery = true)
    List<Activity> findAll(@Param("roomID") int roomID);

    @Query(value = "SELECT * FROM activity where expense_id IS NULL AND room_id = :roomID", nativeQuery = true)
    List<Activity> findAllForSettleUp(@Param("roomID") int roomID);
}
