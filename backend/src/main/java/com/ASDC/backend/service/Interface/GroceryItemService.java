package com.ASDC.backend.service.Interface;


import com.ASDC.backend.dto.RequestDTO.GroceryItemDTORequest;
import com.ASDC.backend.dto.ResponseDTO.GroceryItemDTOResponse;
import com.ASDC.backend.entity.User;

import java.util.List;

public interface GroceryItemService {

    GroceryItemDTOResponse addItem(GroceryItemDTORequest groceryItemDTORequest, User user);
    GroceryItemDTOResponse getItem(int itemID);
    List<GroceryItemDTOResponse> getAllItem(int groceryID);
    GroceryItemDTOResponse updateItem(GroceryItemDTORequest groceryItemDTORequest, boolean updatePurchased, User user);
    boolean deleteItem(int itemID, User user);
}
