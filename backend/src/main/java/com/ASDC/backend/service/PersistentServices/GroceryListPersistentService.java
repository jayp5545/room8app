package com.ASDC.backend.service.PersistentServices;

import com.ASDC.backend.entity.GroceryList;
import com.ASDC.backend.exception.CustomException;
import com.ASDC.backend.repository.GroceryListRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GroceryListPersistentService {

    private final GroceryListRepository groceryListRepository;

    public GroceryListPersistentService(GroceryListRepository groceryListRepository) {
        this.groceryListRepository = groceryListRepository;
    }

    public GroceryList addGroceryList(GroceryList groceryList) {
        return groceryListRepository.save(groceryList);
    }

    public GroceryList getGroceryListById(int id) {
        return groceryListRepository.findById(id)
                .orElseThrow(() -> new CustomException("404", "Grocery list not found!"));
    }

    public List<GroceryList> getAllGroceryListsByRoomId(int roomId) {
        return groceryListRepository.findAllByRoomID(roomId)
                .orElseThrow(() -> new CustomException("400", "Error fetching data!"));
    }

    public void deleteGroceryListById(int id) {
        if (!groceryListRepository.existsById(id)) {
            throw new CustomException("404", "Grocery list not found!");
        }
        groceryListRepository.deleteById(id);
    }

    public Optional<GroceryList> findByName(String listName) {
        return groceryListRepository.findByName(listName);
    }

    public boolean findById(int id) {
        return groceryListRepository.existsById(id);
    }
}