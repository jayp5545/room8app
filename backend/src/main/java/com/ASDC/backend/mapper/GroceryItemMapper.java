package com.ASDC.backend.mapper;

import com.ASDC.backend.dto.RequestDTO.GroceryItemDTORequest;
import com.ASDC.backend.dto.ResponseDTO.GroceryItemDTOResponse;
import com.ASDC.backend.entity.GroceryItems;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

/**
 * Mapper class for converting GroceryItems entities to various DTO responses and vice versa.
 */
@Component
@RequiredArgsConstructor
public class GroceryItemMapper {

    private static final Logger logger = LogManager.getLogger(GroceryItemMapper.class);

    private final UserMapper userMapper;

    /**
     * Converts a GroceryItemDTORequest to a GroceryItems entity.
     *
     * @param groceryItemDTORequest the GroceryItemDTORequest
     * @return the GroceryItems entity
     */
    public GroceryItems toGroceryItem(GroceryItemDTORequest groceryItemDTORequest) {
        if (groceryItemDTORequest == null) {
            logger.warn("Attempted to map null GroceryItemDTORequest to GroceryItems");
            return null;
        }

        GroceryItems groceryItems = new GroceryItems();
        groceryItems.setName(groceryItemDTORequest.getName());
        groceryItems.setQuantity(groceryItemDTORequest.getQuantity());
        groceryItems.setNote(groceryItemDTORequest.getNote());

        logger.info("Mapped GroceryItemDTORequest to GroceryItems: {}", groceryItems);
        return groceryItems;
    }

    /**
     * Converts a GroceryItems entity to a GroceryItemDTOResponse.
     *
     * @param groceryItems the GroceryItems entity
     * @return the GroceryItemDTOResponse
     */
    public GroceryItemDTOResponse toGroceryItemDTOResponse(GroceryItems groceryItems) {
        if (groceryItems == null) {
            logger.warn("Attempted to map null GroceryItems to GroceryItemDTOResponse");
            return null;
        }

        GroceryItemDTOResponse groceryItemDTOResponse = new GroceryItemDTOResponse();
        groceryItemDTOResponse.setId(groceryItems.getId());
        groceryItemDTOResponse.setName(groceryItems.getName());
        groceryItemDTOResponse.setQuantity(groceryItems.getQuantity());
        groceryItemDTOResponse.setNote(groceryItems.getNote());
        groceryItemDTOResponse.setAdded_on(groceryItems.getAdded_on());
        groceryItemDTOResponse.setLast_modified_on(groceryItems.getLast_modified_on());
        groceryItemDTOResponse.setPurchased(groceryItems.isPurchased());
        groceryItemDTOResponse.setAdded_by(userMapper.toUserDTOResponse(groceryItems.getAdded_by()));
        groceryItemDTOResponse.setLast_modified_by(userMapper.toUserDTOResponse(groceryItems.getLast_modified_by()));

        logger.info("Mapped GroceryItems to GroceryItemDTOResponse: {}", groceryItemDTOResponse);
        return groceryItemDTOResponse;
    }
}