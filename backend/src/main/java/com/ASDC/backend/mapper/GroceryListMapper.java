package com.ASDC.backend.mapper;

import com.ASDC.backend.dto.RequestDTO.GrocerylistDTORequest;
import com.ASDC.backend.dto.ResponseDTO.GroceryItemDTOResponse;
import com.ASDC.backend.dto.ResponseDTO.GrocerylistDTOResponse;
import com.ASDC.backend.entity.GroceryItems;
import com.ASDC.backend.entity.GroceryList;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Mapper class for converting GroceryList entities to various DTO responses and vice versa.
 */
@Component
@RequiredArgsConstructor
public class GroceryListMapper {

    private static final Logger logger = LogManager.getLogger(GroceryListMapper.class);

    private final UserMapper userMapper;
    private final RoomMapper roomMapper;
    private final GroceryItemMapper groceryItemMapper;

    /**
     * Converts a GrocerylistDTORequest to a GroceryList entity.
     *
     * @param grocerylistDTORequest the GrocerylistDTORequest
     * @return the GroceryList entity
     */
    public GroceryList toGroceryList(GrocerylistDTORequest grocerylistDTORequest) {
        if (grocerylistDTORequest == null) {
            logger.warn("Attempted to map null GrocerylistDTORequest to GroceryList");
            return null;
        }

        GroceryList groceryList = new GroceryList();
        groceryList.setName(grocerylistDTORequest.getName());

        logger.info("Mapped GrocerylistDTORequest to GroceryList: {}", groceryList);
        return groceryList;
    }

    /**
     * Converts a GroceryList entity to a GrocerylistDTOResponse.
     *
     * @param groceryList the GroceryList entity
     * @return the GrocerylistDTOResponse
     */
    public GrocerylistDTOResponse toGroceryListResponse(GroceryList groceryList) {
        if (groceryList == null) {
            logger.warn("Attempted to map null GroceryList to GrocerylistDTOResponse");
            return null;
        }

        GrocerylistDTOResponse grocerylistResponse = new GrocerylistDTOResponse();
        grocerylistResponse.setId(groceryList.getId());
        grocerylistResponse.setName(groceryList.getName());
        grocerylistResponse.setItems(groceryList.getItems());
        grocerylistResponse.setItems_purchased(groceryList.getItems_purchased());
        grocerylistResponse.setDate_of_creation(groceryList.getDate_of_creation());
        grocerylistResponse.setLast_modified_on(groceryList.getLast_modified_on());
        grocerylistResponse.setCreated_by(userMapper.toUserDTOResponse(groceryList.getCreated_by()));
        grocerylistResponse.setLast_modified_by(userMapper.toUserDTOResponse(groceryList.getLast_modified_by()));
        grocerylistResponse.setActive(groceryList.isActive());
        grocerylistResponse.setRoom(roomMapper.toRoomDTOResponse(groceryList.getRoom()));

        if (groceryList.getGrocery_items() != null) {
            List<GroceryItemDTOResponse> groceryItemDTO = new ArrayList<>();
            for (GroceryItems item : groceryList.getGrocery_items()) {
                groceryItemDTO.add(groceryItemMapper.toGroceryItemDTOResponse(item));
            }
            grocerylistResponse.setGrocery_items(groceryItemDTO);
        } else {
            grocerylistResponse.setGrocery_items(null);
        }

        logger.info("Mapped GroceryList to GrocerylistDTOResponse: {}", grocerylistResponse);
        return grocerylistResponse;
    }
}