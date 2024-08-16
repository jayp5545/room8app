package com.ASDC.backend.service.implementation;

import com.ASDC.backend.dto.RequestDTO.GroceryItemDTORequest;
import com.ASDC.backend.dto.ResponseDTO.GroceryItemDTOResponse;
import com.ASDC.backend.entity.GroceryItems;
import com.ASDC.backend.entity.GroceryList;
import com.ASDC.backend.entity.User;
import com.ASDC.backend.exception.CustomException;
import com.ASDC.backend.mapper.GroceryItemMapper;
import com.ASDC.backend.service.Interface.GroceryItemService;
import com.ASDC.backend.service.PersistentServices.GroceryItemPersistentService;
import com.ASDC.backend.service.PersistentServices.GroceryListPersistentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of the GroceryItemService interface to handle grocery item operations.
 */
@Service
@RequiredArgsConstructor
public class GroceryItemServiceImpl implements GroceryItemService {
    private static final Logger logger = LogManager.getLogger(GroceryItemServiceImpl.class);
    private final GroceryItemMapper groceryItemMapper;
    private final GroceryItemPersistentService groceryItemPersistentService;
    private final GroceryListServiceImpl groceryListService;
    private final GroceryListPersistentService groceryListPersistentService;

    /**
     * Adds a new grocery item to the specified grocery list.
     *
     * @param groceryItemDTORequest the request DTO containing item details.
     * @param user                  the user performing the operation.
     * @return the response DTO containing added item details.
     */
    @Override
    public GroceryItemDTOResponse addItem(GroceryItemDTORequest groceryItemDTORequest, User user) {
        logger.info("Starting addItem with request: {}", groceryItemDTORequest);

        Optional<GroceryList> optionalList = groceryListService.getGroceryList(groceryItemDTORequest.getGroceryListName());

        if (optionalList.isEmpty()) {
            logger.error("Grocery list '{}' does not exist!", groceryItemDTORequest.getGroceryListName());
            throw new CustomException("400", "Grocery list doesn't exist!");
        }

        GroceryList list = optionalList.get();
        validateItem(groceryItemDTORequest.getName());

        GroceryItems groceryItems = createItem(groceryItemDTORequest, user, list);
        list.setLast_modified_on(LocalDateTime.now());
        list.setLast_modified_by(user);
        list.setItems(list.getItems() + 1);

        GroceryItemDTOResponse response = saveItemToDB(groceryItems, list);
        logger.info("Item added successfully: {}", response);
        return response;
    }

    /**
     * Creates a new grocery item entity from the provided request details.
     *
     * @param groceryItemDTORequest the request DTO containing item details.
     * @param user                  the user performing the operation.
     * @param groceryList           the grocery list to which the item belongs.
     * @return the created grocery item entity.
     */
    private GroceryItems createItem(GroceryItemDTORequest groceryItemDTORequest, User user, GroceryList groceryList) {
        return GroceryItems.builder()
                .name(groceryItemDTORequest.getName())
                .added_on(LocalDateTime.now())
                .last_modified_on(LocalDateTime.now())
                .added_by(user)
                .last_modified_by(user)
                .purchased(false)
                .groceryList(groceryList)
                .note(groceryItemDTORequest.getNote())
                .quantity(groceryItemDTORequest.getQuantity())
                .build();
    }

    /**
     * Saves the grocery item and updates the grocery list in the database.
     *
     * @param groceryItems the grocery item to save.
     * @param groceryList  the grocery list to update.
     * @return the response DTO containing saved item details.
     */
    @Transactional
    private GroceryItemDTOResponse saveItemToDB(GroceryItems groceryItems, GroceryList groceryList) {
        logger.info("Saving item '{}' to database.", groceryItems.getName());

        GroceryItems savedItem = groceryItemPersistentService.createItem(groceryItems);
        groceryListPersistentService.addGroceryList(groceryList);

        return groceryItemMapper.toGroceryItemDTOResponse(savedItem);
    }

    /**
     * Retrieves a grocery item by its ID.
     *
     * @param itemID the ID of the item to retrieve.
     * @return the response DTO containing the item details.
     */
    @Override
    public GroceryItemDTOResponse getItem(int itemID) {
        logger.info("Fetching item with ID: {}", itemID);

        GroceryItems groceryItems = groceryItemPersistentService.getItemById(itemID);
        GroceryList groceryList = groceryItems.getGroceryList();

        GroceryItemDTOResponse response = groceryItemMapper.toGroceryItemDTOResponse(groceryItems);
        response.setGroceryListName(groceryList.getName());

        return response;
    }

    /**
     * Retrieves all grocery items for a specified grocery list.
     *
     * @param groceryListID the ID of the grocery list.
     * @return a list of response DTOs containing the item details.
     */
    @Override
    public List<GroceryItemDTOResponse> getAllItem(int groceryListID) {
        logger.info("Fetching all items for grocery list ID: {}", groceryListID);

        GroceryList groceryList = groceryListPersistentService.getGroceryListById(groceryListID);
        List<GroceryItems> groceryItems = groceryItemPersistentService.getAllItemsByGroceryListId(groceryList.getId());

        List<GroceryItemDTOResponse> response = new ArrayList<>();
        for (GroceryItems item : groceryItems) {
            GroceryItemDTOResponse dto = groceryItemMapper.toGroceryItemDTOResponse(item);
            dto.setGroceryListName(groceryList.getName());
            response.add(dto);
        }

        return response;
    }

    /**
     * Updates a grocery item with the provided details.
     *
     * @param groceryItemDTORequest the request DTO containing updated item details.
     * @param updatePurchased       whether to update the purchased status.
     * @param user                  the user performing the operation.
     * @return the response DTO containing updated item details.
     */
    @Override
    @Transactional
    public GroceryItemDTOResponse updateItem(GroceryItemDTORequest groceryItemDTORequest, boolean updatePurchased, User user) {
        logger.info("Updating item with request: {}", groceryItemDTORequest);

        Optional<GroceryList> optionalGroceryList = groceryListPersistentService.findByName(groceryItemDTORequest.getGroceryListName());

        if (optionalGroceryList.isEmpty()) {
            logger.error("Grocery list not found!");
            throw new CustomException("400", "Grocery list not found!");
        }

        GroceryItems item = groceryItemPersistentService.getItemById(groceryItemDTORequest.getId());
        GroceryList groceryList = item.getGroceryList();

        GroceryItemDTOResponse response;
        if (updatePurchased) {
            response = updateItemPurchased(item, groceryList, groceryItemDTORequest, user);
        } else {
            response = updateItemDetails(item, groceryList, groceryItemDTORequest, user);
        }

        logger.info("Item updated successfully: {}", response);
        return response;
    }

    /**
     * Deletes a grocery item by its ID.
     *
     * @param itemID the ID of the item to delete.
     * @param user   the user performing the operation.
     * @return true if the item was successfully deleted, false otherwise.
     */
    @Override
    @Transactional
    public boolean deleteItem(int itemID, User user) {
        logger.info("Deleting item with ID: {}", itemID);

        GroceryItems item = groceryItemPersistentService.getItemById(itemID);
        GroceryList groceryList = item.getGroceryList();

        boolean result = deleteItemFromDB(item, groceryList, user);
        logger.info("Item with ID {} deleted successfully: {}", itemID, result);
        return result;
    }

    /**
     * Validates if an item with the specified name already exists.
     *
     * @param name the name of the item to validate.
     */
    private void validateItem(String name) {
        Optional<GroceryItems> optionalItem = groceryItemPersistentService.getItemByName(name);

        if (optionalItem.isPresent()) {
            logger.error("Item '{}' already exists!", name);
            throw new CustomException("400", "Item already exists!");
        }
    }

    /**
     * Updates the details of a grocery item.
     *
     * @param item                  the item to update.
     * @param groceryList           the grocery list to which the item belongs.
     * @param groceryItemDTORequest the request DTO containing updated item details.
     * @param user                  the user performing the operation.
     * @return the response DTO containing updated item details.
     */
    private GroceryItemDTOResponse updateItemDetails(GroceryItems item, GroceryList groceryList, GroceryItemDTORequest groceryItemDTORequest, User user) {
        logger.info("Updating item details for item ID: {}", item.getId());


        item.setName(groceryItemDTORequest.getName());
        item.setQuantity(groceryItemDTORequest.getQuantity());
        item.setNote(groceryItemDTORequest.getNote());
        item.setLast_modified_on(LocalDateTime.now());
        item.setLast_modified_by(user);

        groceryList.setLast_modified_on(LocalDateTime.now());
        groceryList.setLast_modified_by(user);

        GroceryItems updatedItem = groceryItemPersistentService.createItem(item);
        groceryListPersistentService.addGroceryList(groceryList);

        return groceryItemMapper.toGroceryItemDTOResponse(updatedItem);
    }

    /**
     * Updates the purchased status of a grocery item.
     *
     * @param item                  the item to update.
     * @param groceryList           the grocery list to which the item belongs.
     * @param groceryItemDTORequest the request DTO containing updated item details.
     * @param user                  the user performing the operation.
     * @return the response DTO containing updated item details.
     */
    private GroceryItemDTOResponse updateItemPurchased(GroceryItems item, GroceryList groceryList, GroceryItemDTORequest groceryItemDTORequest, User user) {
        logger.info("Updating purchased status for item ID: {}", item.getId());

        if (item.isPurchased()) {
            logger.error("Item ID {} is already purchased!", item.getId());
            throw new CustomException("400", "Item is already purchased!");
        }

        item.setPurchased(true);
        item.setLast_modified_on(LocalDateTime.now());
        item.setLast_modified_by(user);

        groceryList.setLast_modified_on(LocalDateTime.now());
        groceryList.setLast_modified_by(user);
        groceryList.setItems_purchased(groceryList.getItems_purchased()+1);

        GroceryItems updatedItem = groceryItemPersistentService.createItem(item);
        groceryListPersistentService.addGroceryList(groceryList);

        return groceryItemMapper.toGroceryItemDTOResponse(updatedItem);
    }

    /**
     * Deletes the grocery item from the database and updates the grocery list.
     *
     * @param item        the item to delete.
     * @param groceryList the grocery list to update.
     * @param user        the user performing the operation.
     * @return true if the item was successfully deleted, false otherwise.
     */
    private boolean deleteItemFromDB(GroceryItems item, GroceryList groceryList, User user) {
        logger.info("Deleting item '{}' from database.", item.getName());

        groceryItemPersistentService.deleteItem(item.getId());

        if (item.isPurchased()){
            groceryList.setItems_purchased(groceryList.getItems_purchased()-1);
        }

        groceryList.setLast_modified_on(LocalDateTime.now());
        groceryList.setLast_modified_by(user);
        groceryList.setItems(groceryList.getItems() - 1);

        groceryListPersistentService.addGroceryList(groceryList);
        return true;
    }
}