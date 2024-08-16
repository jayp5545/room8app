package com.ASDC.backend.repository;

import com.ASDC.backend.entity.Expense;
import com.ASDC.backend.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Integer> {

    List<Expense> findAllByRoom(Room room);

    @Query(value = "SELECT * FROM expense WHERE room_id = :roomId AND status = true", nativeQuery = true)
    List<Expense> findAllByRoomActive(int roomId);

    @Query(value = "SELECT * FROM expense WHERE room_id = :roomId AND status = false", nativeQuery = true)
    List<Expense> findAllByRoomInActive(int roomId);

    @Query(value = "SELECT * FROM expense WHERE id = :expenseID AND status = 1", nativeQuery = true)
    Optional<Expense> findActiveById(int expenseID);
}