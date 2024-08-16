package com.ASDC.backend.repository;

import com.ASDC.backend.entity.GroceryList;
import com.ASDC.backend.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query(value = "SELECT * FROM task where assigned_to = :assigned_to", nativeQuery = true)
    List<Task> findByAssignedTo(@Param("assigned_to") String assigned_to);

    Optional<Task> findByName(String name);

    @Query(value = "SELECT * FROM task where room_id = :roomID", nativeQuery = true)
    Optional<List<Task>> findAllByRoomID(@Param("roomID") int roomID);
}

