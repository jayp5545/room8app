package com.ASDC.backend.service;

import com.ASDC.backend.DummyData.DummyData;
import com.ASDC.backend.dto.RequestDTO.GroceryItemDTORequest;
import com.ASDC.backend.dto.ResponseDTO.GroceryItemDTOResponse;
import com.ASDC.backend.entity.GroceryItems;
import com.ASDC.backend.entity.GroceryList;
import com.ASDC.backend.entity.Room;
import com.ASDC.backend.entity.User;
import com.ASDC.backend.exception.CustomException;
import com.ASDC.backend.mapper.GroceryItemMapper;
import com.ASDC.backend.service.PersistentServices.GroceryItemPersistentService;
import com.ASDC.backend.service.implementation.GroceryItemServiceImpl;
import com.ASDC.backend.service.PersistentServices.GroceryListPersistentService;
import com.ASDC.backend.service.implementation.GroceryListServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GroceryItemServiceImplTest {

    @Mock
    private GroceryItemMapper groceryItemMapper;
    @Mock
    private GroceryItemPersistentService groceryItemPersistentService;
    @Mock
    private GroceryListPersistentService groceryListPersistentService;
    @Mock
    private GroceryListServiceImpl groceryListService;
    @InjectMocks
    private GroceryItemServiceImpl groceryItemService;
    private GroceryList groceryList;
    private GroceryItems groceryItems;
    private GroceryItemDTOResponse groceryItemDTOResponse;
    private GroceryItemDTORequest groceryItemDTORequest;
    private User user;
    private Room room;
    private DummyData dummyData;

    @BeforeEach
    public void setUp() {
        dummyData = new DummyData();
        setDummyData();
    }

    private void setDummyData(){
        user = dummyData.createUser();
        room = dummyData.createRoom();
        groceryList = dummyData.createGroceryList();
        groceryItems = dummyData.createGroceryItems();
        groceryItemDTORequest = dummyData.createGroceryItemDTORequest();
        groceryItemDTOResponse = dummyData.createGroceryItemDTOResponse();
    }

    @Test
    public void addItem_Success() {
        when(groceryItemPersistentService.getItemByName(groceryItemDTORequest.getName())).thenReturn(Optional.empty());
        when(groceryListService.getGroceryList(groceryItemDTORequest.getGroceryListName())).thenReturn(Optional.of(groceryList));
        when(groceryItemPersistentService.createItem(any(GroceryItems.class))).thenReturn(groceryItems);
        when(groceryListPersistentService.addGroceryList(any(GroceryList.class))).thenReturn(groceryList);
        when(groceryItemMapper.toGroceryItemDTOResponse(any(GroceryItems.class))).thenReturn(groceryItemDTOResponse);

        GroceryItemDTOResponse createdItem = groceryItemService.addItem(groceryItemDTORequest, user);

        assertNotNull(createdItem);
        assertEquals(groceryItemDTOResponse, createdItem);
        verify(groceryItemPersistentService, times(1)).getItemByName(groceryItemDTORequest.getName());
        verify(groceryListService, times(1)).getGroceryList(groceryItemDTORequest.getGroceryListName());
        verify(groceryItemPersistentService, times(1)).createItem(any(GroceryItems.class));
        verify(groceryListPersistentService, times(1)).addGroceryList(any(GroceryList.class));
        verify(groceryItemMapper, times(1)).toGroceryItemDTOResponse(any(GroceryItems.class));
    }

    @Test
    public void addItem_Failure_ItemAlreadyExists() {
        when(groceryListService.getGroceryList(groceryItemDTORequest.getGroceryListName())).thenReturn(Optional.of(groceryList));
        when(groceryItemPersistentService.getItemByName(groceryItemDTORequest.getName())).thenReturn(Optional.of(groceryItems));

        CustomException exception = assertThrows(CustomException.class, () -> groceryItemService.addItem(groceryItemDTORequest, user));

        assertEquals("400", exception.getErrorCode());
        assertEquals("Item already exists!", exception.getErrorMessage());
        verify(groceryItemPersistentService, times(1)).getItemByName(groceryItemDTORequest.getName());
    }

    @Test
    public void addItem_Failure_ListDoesNotExist() {
        when(groceryListService.getGroceryList(groceryItemDTORequest.getGroceryListName())).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> groceryItemService.addItem(groceryItemDTORequest, user));

        assertEquals("400", exception.getErrorCode());
        assertEquals("Grocery list doesn't exist!", exception.getErrorMessage());
    }

    @Test
    public void getItem_Success() {
        when(groceryItemPersistentService.getItemById(groceryItems.getId())).thenReturn(groceryItems);
        when(groceryItemMapper.toGroceryItemDTOResponse(groceryItems)).thenReturn(groceryItemDTOResponse);

        GroceryItemDTOResponse result = groceryItemService.getItem(groceryItems.getId());

        assertNotNull(result);
        assertEquals(groceryItems.getId(), result.getId());
        assertEquals(groceryItemDTOResponse.getName(), result.getName());
        verify(groceryItemPersistentService, times(1)).getItemById(groceryItems.getId());
        verify(groceryItemMapper, times(1)).toGroceryItemDTOResponse(groceryItems);
    }

    @Test
    public void getItem_Failure_ItemDoesNotExist() {
        when(groceryItemPersistentService.getItemById(groceryItems.getId())).thenThrow(new CustomException("404", "Item not found!"));

        CustomException exception = assertThrows(CustomException.class, () -> groceryItemService.getItem(groceryItems.getId()));

        assertEquals("404", exception.getErrorCode());
        assertEquals("Item not found!", exception.getErrorMessage());
        verify(groceryItemPersistentService, times(1)).getItemById(groceryItems.getId());
    }

    @Test
    public void getAllItem_Success() {
        List<GroceryItems> items = new ArrayList<>();
        items.add(groceryItems);

        when(groceryListPersistentService.getGroceryListById(groceryList.getId())).thenReturn(groceryList);
        when(groceryItemPersistentService.getAllItemsByGroceryListId(groceryList.getId())).thenReturn(items);
        when(groceryItemMapper.toGroceryItemDTOResponse(groceryItems)).thenReturn(groceryItemDTOResponse);

        List<GroceryItemDTOResponse> result = groceryItemService.getAllItem(groceryList.getId());

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(groceryItemDTOResponse.getId(), result.get(0).getId());
        verify(groceryItemPersistentService, times(1)).getAllItemsByGroceryListId(groceryList.getId());
        verify(groceryItemMapper, times(1)).toGroceryItemDTOResponse(groceryItems);
    }

    @Test
    public void getAllItem_Success_EmptyList() {
        when(groceryListPersistentService.getGroceryListById(groceryList.getId())).thenReturn(groceryList);
        when(groceryItemPersistentService.getAllItemsByGroceryListId(groceryList.getId())).thenReturn(new ArrayList<>());

        List<GroceryItemDTOResponse> result = groceryItemService.getAllItem(groceryList.getId());

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(groceryItemPersistentService, times(1)).getAllItemsByGroceryListId(groceryList.getId());
    }

    @Test
    public void updateItem_Success() {
        when(groceryListPersistentService.findByName(groceryItemDTORequest.getGroceryListName()))
                .thenReturn(Optional.of(groceryList));
        when(groceryItemPersistentService.getItemById(groceryItemDTORequest.getId()))
                .thenReturn(groceryItems);
        when(groceryItemMapper.toGroceryItemDTOResponse(groceryItems))
                .thenReturn(groceryItemDTOResponse);
        when(groceryItemPersistentService.createItem(groceryItems))
                .thenReturn(groceryItems);
        when(groceryListPersistentService.addGroceryList(any(GroceryList.class)))
                .thenReturn(groceryList);

        GroceryItemDTOResponse updatedItem = groceryItemService.updateItem(groceryItemDTORequest, false, user);

        assertNotNull(updatedItem);
        assertEquals(groceryItemDTOResponse, updatedItem);
        verify(groceryListPersistentService, times(1)).findByName(groceryItemDTORequest.getGroceryListName());
        verify(groceryItemPersistentService, times(1)).getItemById(groceryItemDTORequest.getId());
        verify(groceryItemPersistentService, times(1)).createItem(groceryItems);
        verify(groceryListPersistentService, times(1)).addGroceryList(any(GroceryList.class));
    }

    @Test
    public void updateItem_Failure_GroceryListNotFound() {
        when(groceryListPersistentService.findByName(groceryItemDTORequest.getGroceryListName())).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> groceryItemService.updateItem(groceryItemDTORequest, false, user));

        assertEquals("400", exception.getErrorCode());
        assertEquals("Grocery list not found!", exception.getErrorMessage());
        verify(groceryListPersistentService, times(1)).findByName(groceryItemDTORequest.getGroceryListName());
    }

    @Test
    public void updateItem_Failure_ItemNotFound() {
        when(groceryListPersistentService.findByName(groceryItemDTORequest.getGroceryListName())).thenReturn(Optional.of(groceryList));
        when(groceryItemPersistentService.getItemById(groceryItemDTORequest.getId())).thenThrow(new CustomException("404", "Item not found!"));

        CustomException exception = assertThrows(CustomException.class, () -> groceryItemService.updateItem(groceryItemDTORequest, false, user));

        assertEquals("404", exception.getErrorCode());
        assertEquals("Item not found!", exception.getErrorMessage());
        verify(groceryListPersistentService, times(1)).findByName(groceryItemDTORequest.getGroceryListName());
        verify(groceryItemPersistentService, times(1)).getItemById(groceryItemDTORequest.getId());
    }

    @Test
    public void deleteItem_Success() {
        when(groceryItemPersistentService.getItemById(groceryItems.getId())).thenReturn(groceryItems);

        groceryItemService.deleteItem(groceryItems.getId(), user);

        verify(groceryItemPersistentService, times(1)).deleteItem(groceryItems.getId());
    }

    @Test
    public void deleteItem_Failure_ItemNotFound() {
        when(groceryItemPersistentService.getItemById(groceryItems.getId())).thenThrow(new CustomException("404", "Item not found!"));

        CustomException exception = assertThrows(CustomException.class, () -> groceryItemService.deleteItem(groceryItems.getId(), user));

        assertEquals("404", exception.getErrorCode());
        assertEquals("Item not found!", exception.getErrorMessage());
        verify(groceryItemPersistentService, times(1)).getItemById(groceryItems.getId());
    }

    @Test
    public void deleteItem_Success_AlreadyPurchased()
    {
        when(groceryItemPersistentService.getItemById(groceryItems.getId())).thenReturn(groceryItems);
        when(groceryListPersistentService.addGroceryList(any(GroceryList.class))).thenReturn(groceryList);

        groceryItems.setPurchased(true);
        groceryItemService.deleteItem(groceryItems.getId(), user);

        verify(groceryItemPersistentService, times(1)).deleteItem(groceryItems.getId());
        verify(groceryListPersistentService, times(1)).addGroceryList(any(GroceryList.class));
    }

    @Test
    public void updateItemPurchased_Success() {
        when(groceryItemPersistentService.getItemById(groceryItemDTORequest.getId())).thenReturn(groceryItems);
        when(groceryListPersistentService.findByName(groceryItemDTORequest.getGroceryListName())).thenReturn(Optional.of(groceryList));
        when(groceryItemMapper.toGroceryItemDTOResponse(groceryItems)).thenReturn(groceryItemDTOResponse);
        when(groceryItemPersistentService.createItem(any(GroceryItems.class))).thenReturn(groceryItems);
        when(groceryListPersistentService.addGroceryList(any(GroceryList.class))).thenReturn(groceryList);

        groceryItems.setPurchased(false);
        GroceryItemDTOResponse response = groceryItemService.updateItem(groceryItemDTORequest, true, user);

        assertNotNull(response);
        verify(groceryItemPersistentService, times(1)).createItem(any(GroceryItems.class));
        verify(groceryListPersistentService, times(1)).addGroceryList(any(GroceryList.class));
    }

    @Test
    public void updateItem_Failure_ItemAlreadyPurchased() {
        when(groceryItemPersistentService.getItemById(groceryItemDTORequest.getId())).thenReturn(groceryItems);
        when(groceryListPersistentService.findByName(groceryItemDTORequest.getGroceryListName())).thenReturn(Optional.of(groceryList));

        groceryItems.setPurchased(true);

        CustomException exception = assertThrows(CustomException.class, () -> groceryItemService.updateItem(groceryItemDTORequest, true, user));

        assertEquals("400", exception.getErrorCode());
        assertEquals("Item is already purchased!", exception.getErrorMessage());
        verify(groceryItemPersistentService, never()).createItem(groceryItems);
        verify(groceryListPersistentService, never()).addGroceryList(groceryList);
    }
}