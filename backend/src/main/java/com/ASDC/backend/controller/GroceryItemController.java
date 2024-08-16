package com.ASDC.backend.controller;

import com.ASDC.backend.dto.RequestDTO.GroceryItemDTORequest;
import com.ASDC.backend.dto.ResponseDTO.GroceryItemDTOResponse;
import com.ASDC.backend.entity.User;
import com.ASDC.backend.exception.CustomException;
import com.ASDC.backend.service.Interface.GroceryItemService;
import com.ASDC.backend.Models.ExtractRequestInfo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for handling grocery item-related API endpoints.
 */
@RestController
@RequestMapping("/api/v1/grocery-list/item")
@RequiredArgsConstructor
public class GroceryItemController {

    private static final Logger logger = LogManager.getLogger(GroceryItemController.class);

    private final GroceryItemService groceryItemService;
    private final ExtractRequestInfo extractRequestInfo;

    /**
     * Add a new grocery item.
     *
     * @param groceryItemDTORequest the grocery item request DTO
     * @return the created grocery item DTO response
     */
    @PostMapping("/add")
    public ResponseEntity<GroceryItemDTOResponse> addGroceryItem(@Valid @RequestBody GroceryItemDTORequest groceryItemDTORequest) {
        User user = extractRequestInfo.getUser();
        logger.info("Adding grocery item for user: {} {}", user.getFirstName(), user.getLastName());

        GroceryItemDTOResponse response = groceryItemService.addItem(groceryItemDTORequest, user);

        logger.info("Added grocery item with ID: {}", response.getId());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Get a grocery item by its ID.
     *
     * @param itemID the ID of the grocery item
     * @return the grocery item DTO response
     */
    @GetMapping("/get/{id}")
    public ResponseEntity<GroceryItemDTOResponse> getGroceryItem(@PathVariable(name = "id") int itemID) {
        validateId(itemID);
        logger.info("Fetching grocery item with ID: {}", itemID);

        GroceryItemDTOResponse response = groceryItemService.getItem(itemID);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Get all grocery items for a specific list.
     *
     * @param listID the ID of the grocery list
     * @return a list of grocery item DTO responses
     */
    @GetMapping("/get/all/{id}")
    public ResponseEntity<List<GroceryItemDTOResponse>> getGroceryAllItem(@PathVariable(name = "id") int listID) {
        validateId(listID);
        logger.info("Fetching all grocery items for list ID: {}", listID);

        List<GroceryItemDTOResponse> response = groceryItemService.getAllItem(listID);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Delete a grocery item by its ID.
     *
     * @param itemID the ID of the grocery item to delete
     * @return a response entity with success status
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> deleteGroceryItem(@PathVariable(name = "id") int itemID) {
        validateId(itemID);
        User user = extractRequestInfo.getUser();

        logger.info("Deleting grocery item with ID: {}", itemID);

        boolean response = groceryItemService.deleteItem(itemID, user);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Update an existing grocery item.
     *
     * @param groceryItemDTORequest the grocery item request DTO
     * @return the updated grocery item DTO response
     */
    @PutMapping("/update")
    public ResponseEntity<GroceryItemDTOResponse> updateGroceryItem(@Valid @RequestBody GroceryItemDTORequest groceryItemDTORequest) {
        User user = extractRequestInfo.getUser();

        logger.info("Updating grocery item for user: {} {}", user.getFirstName(), user.getLastName());

        GroceryItemDTOResponse response = groceryItemService.updateItem(groceryItemDTORequest, false, user);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Mark a grocery item as purchased.
     *
     * @param groceryItemDTORequest the grocery item request DTO
     * @return a response entity with success status
     */
    @PutMapping("/update/purchased")
    public ResponseEntity<Boolean> updateGroceryItemPurchased(@Valid @RequestBody GroceryItemDTORequest groceryItemDTORequest) {
        User user = extractRequestInfo.getUser();

        logger.info("Marking grocery item as purchased for user: {} {}", user.getFirstName(), user.getLastName());

        groceryItemService.updateItem(groceryItemDTORequest, true, user);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    /**
     * Validate the provided ID.
     *
     * @param id the ID to validate
     */
    private void validateId(int id) {
        if (id <= 0) {
            logger.error("Invalid ID: {}", id);
            throw new CustomException("400", "ID must be greater than zero.");
        }
    }
}