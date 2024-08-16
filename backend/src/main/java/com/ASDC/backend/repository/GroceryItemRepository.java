package com.ASDC.backend.repository;

import com.ASDC.backend.entity.GroceryItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface GroceryItemRepository extends JpaRepository<GroceryItems, Integer> {

    Optional<GroceryItems> findByName(String name);

    @Query(value = "SELECT * FROM grocery_items where grocery_id = :groceryID", nativeQuery = true)
    Optional<List<GroceryItems>> findAllByGroceryListID(int groceryID);
}
