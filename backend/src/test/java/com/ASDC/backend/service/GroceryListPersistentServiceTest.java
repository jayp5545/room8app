package com.ASDC.backend.service;

import com.ASDC.backend.DummyData.DummyData;
import com.ASDC.backend.entity.GroceryList;
import com.ASDC.backend.exception.CustomException;
import com.ASDC.backend.repository.GroceryListRepository;
import com.ASDC.backend.service.PersistentServices.GroceryListPersistentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GroceryListPersistentServiceTest {

    @Mock
    private GroceryListRepository groceryListRepository;

    @InjectMocks
    private GroceryListPersistentService groceryListPersistentService;

    private GroceryList groceryList;
    private DummyData dummyData;

    @BeforeEach
    void setUp() {
       dummyData = new DummyData();
       groceryList = dummyData.createGroceryList();
    }

    @Test
    public void addGroceryList_Success() {
        when(groceryListRepository.save(groceryList)).thenReturn(groceryList);

        GroceryList result = groceryListPersistentService.addGroceryList(groceryList);

        assertNotNull(result);
        assertEquals(groceryList.getId(), result.getId());
        verify(groceryListRepository, times(1)).save(groceryList);
    }

    @Test
    public void getGroceryListById_Found() {
        when(groceryListRepository.findById(groceryList.getId())).thenReturn(Optional.of(groceryList));

        GroceryList result = groceryListPersistentService.getGroceryListById(groceryList.getId());

        assertNotNull(result);
        assertEquals(groceryList.getId(), result.getId());
        verify(groceryListRepository, times(1)).findById(groceryList.getId());
    }

    @Test
    public void getGroceryListById_NotFound() {
        when(groceryListRepository.findById(groceryList.getId())).thenReturn(Optional.empty());

        CustomException thrown = assertThrows(CustomException.class, () ->
                groceryListPersistentService.getGroceryListById(groceryList.getId()));

        assertEquals("404", thrown.getErrorCode());
        assertEquals("Grocery list not found!", thrown.getErrorMessage());
        verify(groceryListRepository, times(1)).findById(groceryList.getId());
    }

    @Test
    public void getAllGroceryListsByRoomId_Success() {
        when(groceryListRepository.findAllByRoomID(groceryList.getId())).thenReturn(Optional.of(Arrays.asList(groceryList)));

        List<GroceryList> result = groceryListPersistentService.getAllGroceryListsByRoomId(groceryList.getId());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(groceryList.getId(), result.get(0).getId());
        verify(groceryListRepository, times(1)).findAllByRoomID(groceryList.getId());
    }

    @Test
    public void getAllGroceryListsByRoomId_Error() {
        when(groceryListRepository.findAllByRoomID(groceryList.getId())).thenReturn(Optional.empty());

        CustomException thrown = assertThrows(CustomException.class, () ->
                groceryListPersistentService.getAllGroceryListsByRoomId(groceryList.getId()));

        assertEquals("400", thrown.getErrorCode());
        assertEquals("Error fetching data!", thrown.getErrorMessage());
        verify(groceryListRepository, times(1)).findAllByRoomID(groceryList.getId());
    }

    @Test
    public void deleteGroceryListById_Success() {
        when(groceryListRepository.existsById(groceryList.getId())).thenReturn(true);

        groceryListPersistentService.deleteGroceryListById(groceryList.getId());

        verify(groceryListRepository, times(1)).deleteById(groceryList.getId());
    }

    @Test
    public void deleteGroceryListById_NotFound() {
        when(groceryListRepository.existsById(groceryList.getId())).thenReturn(false);

        CustomException thrown = assertThrows(CustomException.class, () ->
                groceryListPersistentService.deleteGroceryListById(groceryList.getId()));

        assertEquals("404", thrown.getErrorCode());
        assertEquals("Grocery list not found!", thrown.getErrorMessage());
        verify(groceryListRepository, times(1)).existsById(groceryList.getId());
        verify(groceryListRepository, times(0)).deleteById(groceryList.getId());
    }

    @Test
    public void findByName_Found() {
        when(groceryListRepository.findByName(groceryList.getName())).thenReturn(Optional.of(groceryList));

        Optional<GroceryList> result = groceryListPersistentService.findByName(groceryList.getName());

        assertTrue(result.isPresent());
        assertEquals(groceryList.getId(), result.get().getId());
        verify(groceryListRepository, times(1)).findByName(groceryList.getName());
    }

    @Test
    public void findByName_NotFound() {
        when(groceryListRepository.findByName(groceryList.getName())).thenReturn(Optional.empty());

        Optional<GroceryList> result = groceryListPersistentService.findByName(groceryList.getName());

        assertFalse(result.isPresent());
        verify(groceryListRepository, times(1)).findByName(groceryList.getName());
    }

    @Test
    public void findById_Exists() {
        when(groceryListRepository.existsById(groceryList.getId())).thenReturn(true);

        boolean result = groceryListPersistentService.findById(groceryList.getId());

        assertTrue(result);
        verify(groceryListRepository, times(1)).existsById(groceryList.getId());
    }

    @Test
    public void findById_DoesNotExist() {
        when(groceryListRepository.existsById(groceryList.getId())).thenReturn(false);

        boolean result = groceryListPersistentService.findById(groceryList.getId());

        assertFalse(result);
        verify(groceryListRepository, times(1)).existsById(groceryList.getId());
    }
}
