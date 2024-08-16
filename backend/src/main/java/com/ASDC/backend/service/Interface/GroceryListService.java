package com.ASDC.backend.service.Interface;
import com.ASDC.backend.dto.RequestDTO.GrocerylistDTORequest;
import com.ASDC.backend.dto.ResponseDTO.GrocerylistDTOResponse;
import com.ASDC.backend.entity.Room;
import com.ASDC.backend.entity.User;

import java.util.List;

public interface GroceryListService {

    GrocerylistDTOResponse createList(GrocerylistDTORequest grocerylistDTORequest, User user, Room room);

    GrocerylistDTOResponse getList(int groceryListName);

    List<GrocerylistDTOResponse> getAllList(Room room);

    boolean deleteList(int id);
}
