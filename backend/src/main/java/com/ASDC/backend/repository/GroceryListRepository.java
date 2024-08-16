package com.ASDC.backend.repository;

import com.ASDC.backend.entity.GroceryList;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroceryListRepository extends JpaRepository<GroceryList, Integer> {

    Optional<GroceryList> findByName(String name);

    @Query(value = "SELECT * FROM grocery where room_id = :roomID", nativeQuery = true)
    Optional<List<GroceryList>> findAllByRoomID(@Param("roomID") int roomID);
}
