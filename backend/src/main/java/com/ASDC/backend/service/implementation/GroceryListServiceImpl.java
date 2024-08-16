package com.ASDC.backend.service.implementation;

import com.ASDC.backend.dto.RequestDTO.GrocerylistDTORequest;
import com.ASDC.backend.dto.ResponseDTO.GrocerylistDTOResponse;
import com.ASDC.backend.entity.GroceryList;
import com.ASDC.backend.entity.Room;
import com.ASDC.backend.entity.User;
import com.ASDC.backend.exception.CustomException;
import com.ASDC.backend.mapper.GroceryListMapper;
import com.ASDC.backend.service.Interface.GroceryListService;
import com.ASDC.backend.service.PersistentServices.GroceryListPersistentService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of GroceryListService interface.
 * Provides methods to create, retrieve, and delete grocery lists.
 */
@Service
@RequiredArgsConstructor
public class GroceryListServiceImpl implements GroceryListService {

    private static final Logger logger = LogManager.getLogger(GroceryListServiceImpl.class);

    private final GroceryListMapper groceryListMapper;
    private final GroceryListPersistentService groceryListPersistentService;

    /**
     * Creates a new grocery list.
     *
     * @param groceryListDTORequest The request DTO containing grocery list details.
     * @param user                  The user creating the grocery list.
     * @param room                  The room associated with the grocery list.
     * @return The response DTO containing the created grocery list details.
     * @throws CustomException If a grocery list with the same name already exists.
     */
    @Override
    public GrocerylistDTOResponse createList(GrocerylistDTORequest groceryListDTORequest, User user, Room room) {
        logger.info("Starting createList for grocery list: {}", groceryListDTORequest.getName());

        Optional<GroceryList> existingList = getGroceryList(groceryListDTORequest.getName());

        if (existingList.isPresent()) {
            logger.error("Grocery list with name '{}' already exists!", groceryListDTORequest.getName());
            throw new CustomException("400", "Grocery list with this name already exists!");
        }

        GroceryList groceryList = initializeGroceryList(groceryListDTORequest, room, user);
        GroceryList response = groceryListPersistentService.addGroceryList(groceryList);

        logger.info("Grocery list created successfully: {}", response.getName());
        return groceryListMapper.toGroceryListResponse(response);
    }

    /**
     * Retrieves a grocery list by its ID.
     *
     * @param id The ID of the grocery list.
     * @return The response DTO containing the grocery list details.
     */
    @Override
    public GrocerylistDTOResponse getList(int id) {
        logger.info("Fetching grocery list with ID: {}", id);

        GroceryList list = groceryListPersistentService.getGroceryListById(id);
        return groceryListMapper.toGroceryListResponse(list);
    }

    /**
     * Retrieves all grocery lists for a given room.
     *
     * @param room The room for which to retrieve grocery lists.
     * @return A list of response DTOs containing grocery list details.
     */
    @Override
    public List<GrocerylistDTOResponse> getAllList(Room room) {
        logger.info("Fetching all grocery lists for room ID: {}", room.getId());

        List<GroceryList> list = groceryListPersistentService.getAllGroceryListsByRoomId(room.getId());

        List<GrocerylistDTOResponse> response = new ArrayList<>();
        for (GroceryList currList : list) {
            response.add(groceryListMapper.toGroceryListResponse(currList));
        }

        return response;
    }

    /**
     * Deletes a grocery list by its ID.
     *
     * @param id The ID of the grocery list to be deleted.
     * @return True if the grocery list was deleted successfully.
     * @throws CustomException If the grocery list is not found.
     */
    @Override
    public boolean deleteList(int id) {
        logger.info("Deleting grocery list with ID: {}", id);

        if (!groceryListPersistentService.findById(id)) {
            logger.error("Grocery list with ID {} not found!", id);
            throw new CustomException("404", "Grocery list not found!");
        }
        groceryListPersistentService.deleteGroceryListById(id);
        logger.info("Grocery list with ID {} deleted successfully.", id);
        return true;
    }

    /**
     * Retrieves a grocery list by its name.
     *
     * @param listName The name of the grocery list.
     * @return An Optional containing the grocery list if found.
     */
    public Optional<GroceryList> getGroceryList(String listName) {
        return groceryListPersistentService.findByName(listName);
    }

    /**
     * Initializes a new GroceryList entity.
     *
     * @param groceryListDTORequest The request DTO containing grocery list details.
     * @param room                  The room associated with the grocery list.
     * @param user                  The user creating the grocery list.
     * @return The initialized GroceryList entity.
     */
    private GroceryList initializeGroceryList(GrocerylistDTORequest groceryListDTORequest, Room room, User user) {
        GroceryList groceryList = new GroceryList();
        groceryList.setName(groceryListDTORequest.getName());
        groceryList.setRoom(room);
        groceryList.setItems(0);
        groceryList.setItems_purchased(0);
        groceryList.setDate_of_creation(LocalDateTime.now());
        groceryList.setLast_modified_on(LocalDateTime.now());
        groceryList.setCreated_by(user);
        groceryList.setLast_modified_by(user);
        groceryList.setActive(true);
        groceryList.setGrocery_items(new ArrayList<>());
        return groceryList;
    }
}