package com.ASDC.backend.controller;

import com.ASDC.backend.Models.ExtractRequestInfo;
import com.ASDC.backend.dto.RequestDTO.GrocerylistDTORequest;
import com.ASDC.backend.dto.ResponseDTO.GrocerylistDTOResponse;
import com.ASDC.backend.entity.Room;
import com.ASDC.backend.entity.User;
import com.ASDC.backend.exception.CustomException;
import com.ASDC.backend.service.Interface.GroceryListService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for handling grocery list-related API endpoints.
 */
@RestController
@RequestMapping("/api/v1/grocery-list")
@RequiredArgsConstructor
public class GroceryListController {

    private static final Logger logger = LogManager.getLogger(GroceryListController.class);

    private final GroceryListService groceryListService;
    private final ExtractRequestInfo extractRequestInfo;

    /**
     * Add a new grocery list.
     *
     * @param grocerylistDTORequest the grocery list request DTO
     * @return the created grocery list DTO response
     */
        @PostMapping("/add")
        public ResponseEntity<GrocerylistDTOResponse> addGroceryList(@Valid @RequestBody GrocerylistDTORequest grocerylistDTORequest) {
        User user = extractRequestInfo.getUser();
        Room room = extractRequestInfo.getRoom();

        logger.info("Adding grocery list for user: {} {}, room: {}", user.getFirstName(), user.getLastName(), room.getName());

        GrocerylistDTOResponse response = groceryListService.createList(grocerylistDTORequest, user, room);

        logger.info("Added grocery list with Name: {}", response.getName());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Delete a grocery list by its ID.
     *
     * @param id      the ID of the grocery list to delete
     * @param request the HTTP servlet request
     * @return a response entity with success status
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteGroceryList(@PathVariable(name = "id") int id) {
        validateId(id);

        logger.info("Deleting grocery list with ID: {}", id);

        groceryListService.deleteList(id);

        logger.info("Deleted grocery list with ID: {}", id);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    /**
     * Get a grocery list by its ID.
     *
     * @param id the ID of the grocery list
     * @return the grocery list DTO response
     */
    @GetMapping("/get/{id}")
    public ResponseEntity<GrocerylistDTOResponse> getGroceryList(@PathVariable(name = "id") int id) {
        validateId(id);

        logger.info("Fetching grocery list with ID: {}", id);

        GrocerylistDTOResponse response = groceryListService.getList(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Get all grocery lists for a specific room.
     *
     * @param request the HTTP servlet request
     * @return a list of grocery list DTO responses
     */
    @GetMapping("/get/all")
    public ResponseEntity<List<GrocerylistDTOResponse>> getAllGroceryLists() {
        Room room = extractRequestInfo.getRoom();

        logger.info("Fetching all grocery lists for room: {}", room.getName());

        List<GrocerylistDTOResponse> response = groceryListService.getAllList(room);
        return new ResponseEntity<>(response, HttpStatus.OK);
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