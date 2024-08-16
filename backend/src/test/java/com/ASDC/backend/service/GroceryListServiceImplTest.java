package com.ASDC.backend.service;

import com.ASDC.backend.DummyData.DummyData;
import com.ASDC.backend.dto.RequestDTO.GrocerylistDTORequest;
import com.ASDC.backend.dto.ResponseDTO.GrocerylistDTOResponse;
import com.ASDC.backend.entity.GroceryList;
import com.ASDC.backend.entity.Room;
import com.ASDC.backend.entity.User;
import com.ASDC.backend.exception.CustomException;
import com.ASDC.backend.mapper.GroceryListMapper;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GroceryListServiceImplTest {

    @Mock
    private GroceryListMapper groceryListMapper;

    @Mock
    private GroceryListPersistentService groceryListPersistentService;

    @InjectMocks
    private GroceryListServiceImpl groceryListService;

    private DummyData dummyData;
    private GroceryList groceryList;
    private GrocerylistDTORequest groceryListDTORequest;
    private GrocerylistDTOResponse groceryListDTOResponse;
    private User user;
    private Room room;

    @BeforeEach
    public void setUp() {
        dummyData = new DummyData();
        setDummyData();
    }

    private void setDummyData() {
        user = dummyData.createUser();
        room = dummyData.createRoom();
        groceryList = dummyData.createGroceryList();
        groceryListDTORequest = dummyData.createGroceryListDTORequest();
        groceryListDTOResponse = dummyData.createGroceryListDTOResponse();
    }

    @Test
    public void createList_Success() {
        when(groceryListPersistentService.findByName(groceryListDTORequest.getName())).thenReturn(Optional.empty());
        when(groceryListPersistentService.addGroceryList(any(GroceryList.class))).thenReturn(groceryList);
        when(groceryListMapper.toGroceryListResponse(groceryList)).thenReturn(groceryListDTOResponse);

        GrocerylistDTOResponse createdList = groceryListService.createList(groceryListDTORequest, user, room);

        assertNotNull(createdList);
        assertEquals(groceryListDTORequest.getName(), createdList.getName());

        verify(groceryListPersistentService, times(1)).findByName(groceryListDTORequest.getName());
        verify(groceryListPersistentService, times(1)).addGroceryList(any(GroceryList.class));
        verify(groceryListMapper, times(1)).toGroceryListResponse(groceryList);
    }

    @Test
    public void createList_Failure_ListAlreadyExists() {
        when(groceryListPersistentService.findByName(groceryListDTORequest.getName())).thenReturn(Optional.of(groceryList));

        CustomException thrown = assertThrows(CustomException.class, () -> {
            groceryListService.createList(groceryListDTORequest, user, room);
        });

        assertEquals("400", thrown.getErrorCode());
        assertEquals("Grocery list with this name already exists!", thrown.getErrorMessage());

        verify(groceryListPersistentService, times(1)).findByName(groceryListDTORequest.getName());
        verify(groceryListPersistentService, times(0)).addGroceryList(any(GroceryList.class));
    }

    @Test
    public void getList_Success() {
        when(groceryListPersistentService.getGroceryListById(groceryList.getId())).thenReturn(groceryList);
        when(groceryListMapper.toGroceryListResponse(groceryList)).thenReturn(groceryListDTOResponse);

        GrocerylistDTOResponse result = groceryListService.getList(groceryList.getId());

        assertNotNull(result);
        assertEquals(groceryListDTOResponse, result);

        verify(groceryListPersistentService, times(1)).getGroceryListById(groceryList.getId());
        verify(groceryListMapper, times(1)).toGroceryListResponse(groceryList);
    }

    @Test
    public void getList_Failure_ListDoesNotExist() {
        when(groceryListPersistentService.getGroceryListById(groceryList.getId())).thenThrow(new CustomException("404", "Grocery list not found!"));

        CustomException exception = assertThrows(CustomException.class, () -> {
            groceryListService.getList(groceryList.getId());
        });

        assertEquals("404", exception.getErrorCode());
        assertEquals("Grocery list not found!", exception.getErrorMessage());

        verify(groceryListPersistentService, times(1)).getGroceryListById(groceryList.getId());
        verify(groceryListMapper, times(0)).toGroceryListResponse(any(GroceryList.class));
    }

    @Test
    public void getAllList_Success() {
        List<GroceryList> groceryLists = new ArrayList<>();
        groceryLists.add(groceryList);
        List<GrocerylistDTOResponse> responseDTOList = new ArrayList<>();
        responseDTOList.add(groceryListDTOResponse);

        when(groceryListPersistentService.getAllGroceryListsByRoomId(room.getId())).thenReturn(groceryLists);
        when(groceryListMapper.toGroceryListResponse(groceryList)).thenReturn(groceryListDTOResponse);

        List<GrocerylistDTOResponse> result = groceryListService.getAllList(room);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(responseDTOList.size(), result.size());
        assertEquals(responseDTOList.get(0).getName(), result.get(0).getName());

        verify(groceryListPersistentService, times(1)).getAllGroceryListsByRoomId(room.getId());
        verify(groceryListMapper, times(1)).toGroceryListResponse(groceryList);
    }

    @Test
    public void getAllList_Failure_SomethingWentWrong() {
        when(groceryListPersistentService.getAllGroceryListsByRoomId(room.getId())).thenReturn(new ArrayList<>());

        List<GrocerylistDTOResponse> result = groceryListService.getAllList(room);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(groceryListPersistentService, times(1)).getAllGroceryListsByRoomId(room.getId());
        verify(groceryListMapper, times(0)).toGroceryListResponse(any(GroceryList.class));
    }

    @Test
    public void deleteList_Success() {
        when(groceryListPersistentService.findById(groceryList.getId())).thenReturn(true);

        boolean result = groceryListService.deleteList(groceryList.getId());

        assertTrue(result);

        verify(groceryListPersistentService, times(1)).findById(groceryList.getId());
        verify(groceryListPersistentService, times(1)).deleteGroceryListById(groceryList.getId());
    }

    @Test
    public void deleteList_Failure_ListDoesNotExist() {
        when(groceryListPersistentService.findById(groceryList.getId())).thenReturn(false);

        CustomException exception = assertThrows(CustomException.class, () -> {
            groceryListService.deleteList(groceryList.getId());
        });

        assertEquals("404", exception.getErrorCode());
        assertEquals("Grocery list not found!", exception.getErrorMessage());

        verify(groceryListPersistentService, times(1)).findById(groceryList.getId());
        verify(groceryListPersistentService, times(0)).deleteGroceryListById(anyInt());
    }

    @Test
    public void deleteList_Failure_SomethingWentWrong() {
        when(groceryListPersistentService.findById(groceryList.getId())).thenReturn(true);
        doThrow(new CustomException("500", "Internal Server Error")).when(groceryListPersistentService).deleteGroceryListById(groceryList.getId());

        CustomException exception = assertThrows(CustomException.class, () -> {
            groceryListService.deleteList(groceryList.getId());
        });

        assertEquals("500", exception.getErrorCode());
        assertEquals("Internal Server Error", exception.getErrorMessage());

        verify(groceryListPersistentService, times(1)).findById(groceryList.getId());
        verify(groceryListPersistentService, times(1)).deleteGroceryListById(groceryList.getId());
    }
}