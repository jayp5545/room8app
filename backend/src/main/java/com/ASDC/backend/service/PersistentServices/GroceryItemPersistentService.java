package com.ASDC.backend.service.PersistentServices;

import com.ASDC.backend.entity.GroceryItems;
import com.ASDC.backend.exception.CustomException;
import com.ASDC.backend.repository.GroceryItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GroceryItemPersistentService {

    private final GroceryItemRepository groceryItemRepository;

    public GroceryItemPersistentService(GroceryItemRepository groceryItemRepository) {
        this.groceryItemRepository = groceryItemRepository;
    }

    public GroceryItems createItem(GroceryItems groceryItems) {
        return groceryItemRepository.save(groceryItems);
    }

    public GroceryItems getItemById(int id) {
        return groceryItemRepository.findById(id)
                .orElseThrow(() -> new CustomException("404", "Item not found!"));
    }

    public void deleteItem(int id) {
        if (!groceryItemRepository.existsById(id)) {
            throw new CustomException("404", "Item not found!");
        }
        groceryItemRepository.deleteById(id);
    }

    public Optional<GroceryItems> getItemByName(String name) {
        return groceryItemRepository.findByName(name);
    }

    public List<GroceryItems> getAllItemsByGroceryListId(int groceryListId) {
        return groceryItemRepository.findAllByGroceryListID(groceryListId)
                .orElseThrow(() -> new CustomException("400", "No items found for the provided grocery list ID!"));
    }
}