package com.ASDC.backend.service;

import com.ASDC.backend.DummyData.DummyData;
import com.ASDC.backend.entity.GroceryItems;
import com.ASDC.backend.exception.CustomException;
import com.ASDC.backend.repository.GroceryItemRepository;
import com.ASDC.backend.service.PersistentServices.GroceryItemPersistentService;
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
public class GroceryItemPersistentServiceTest {

    @Mock
    private GroceryItemRepository groceryItemRepository;

    @InjectMocks
    private GroceryItemPersistentService groceryItemPersistentService;

    private GroceryItems groceryItem;
    private DummyData dummyData;

    @BeforeEach
    void setUp() {
        dummyData = new DummyData();
        groceryItem = dummyData.createGroceryItems();
    }

    @Test
    public void createItem_Success() {
        when(groceryItemRepository.save(groceryItem)).thenReturn(groceryItem);

        GroceryItems result = groceryItemPersistentService.createItem(groceryItem);

        assertNotNull(result);
        assertEquals(groceryItem.getId(), result.getId());
        verify(groceryItemRepository, times(1)).save(groceryItem);
    }

    @Test
    public void getItemById_Found() {
        when(groceryItemRepository.findById(groceryItem.getId())).thenReturn(Optional.of(groceryItem));

        GroceryItems result = groceryItemPersistentService.getItemById(groceryItem.getId());

        assertNotNull(result);
        assertEquals(groceryItem.getId(), result.getId());
        verify(groceryItemRepository, times(1)).findById(groceryItem.getId());
    }

    @Test
    public void getItemById_NotFound() {
        when(groceryItemRepository.findById(groceryItem.getId())).thenReturn(Optional.empty());

        CustomException thrown = assertThrows(CustomException.class, () ->
                groceryItemPersistentService.getItemById(groceryItem.getId()));

        assertEquals("404", thrown.getErrorCode());
        assertEquals("Item not found!", thrown.getErrorMessage());
        verify(groceryItemRepository, times(1)).findById(groceryItem.getId());
    }

    @Test
    public void deleteItem_Success() {
        when(groceryItemRepository.existsById(groceryItem.getId())).thenReturn(true);

        groceryItemPersistentService.deleteItem(groceryItem.getId());

        verify(groceryItemRepository, times(1)).deleteById(groceryItem.getId());
    }

    @Test
    public void deleteItem_NotFound() {
        when(groceryItemRepository.existsById(groceryItem.getId())).thenReturn(false);

        CustomException thrown = assertThrows(CustomException.class, () ->
                groceryItemPersistentService.deleteItem(groceryItem.getId()));

        assertEquals("404", thrown.getErrorCode());
        assertEquals("Item not found!", thrown.getErrorMessage());
        verify(groceryItemRepository, times(1)).existsById(groceryItem.getId());
        verify(groceryItemRepository, times(0)).deleteById(groceryItem.getId()); // Ensure deleteById is not called
    }

    @Test
    public void getItemByName_Found() {
        when(groceryItemRepository.findByName(groceryItem.getName())).thenReturn(Optional.of(groceryItem));

        Optional<GroceryItems> result = groceryItemPersistentService.getItemByName(groceryItem.getName());

        assertTrue(result.isPresent());
        assertEquals(groceryItem.getId(), result.get().getId());
        verify(groceryItemRepository, times(1)).findByName(groceryItem.getName());
    }

    @Test
    public void getItemByName_NotFound() {
        when(groceryItemRepository.findByName(groceryItem.getName())).thenReturn(Optional.empty());

        Optional<GroceryItems> result = groceryItemPersistentService.getItemByName(groceryItem.getName());

        assertFalse(result.isPresent());
        verify(groceryItemRepository, times(1)).findByName(groceryItem.getName());
    }

    @Test
    public void getAllItemsByGroceryListId_Found() {
        when(groceryItemRepository.findAllByGroceryListID(groceryItem.getId())).thenReturn(Optional.of(Arrays.asList(groceryItem)));

        List<GroceryItems> result = groceryItemPersistentService.getAllItemsByGroceryListId(groceryItem.getId());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(groceryItem.getId(), result.get(0).getId());
        verify(groceryItemRepository, times(1)).findAllByGroceryListID(groceryItem.getId());
    }

    @Test
    public void getAllItemsByGroceryListId_NotFound() {
        when(groceryItemRepository.findAllByGroceryListID(groceryItem.getId())).thenReturn(Optional.empty());

        CustomException thrown = assertThrows(CustomException.class, () ->
                groceryItemPersistentService.getAllItemsByGroceryListId(groceryItem.getId()));

        assertEquals("400", thrown.getErrorCode());
        assertEquals("No items found for the provided grocery list ID!", thrown.getErrorMessage());
        verify(groceryItemRepository, times(1)).findAllByGroceryListID(groceryItem.getId());
    }

}
