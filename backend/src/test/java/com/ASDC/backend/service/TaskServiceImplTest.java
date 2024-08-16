package com.ASDC.backend.service;

import com.ASDC.backend.DummyData.DummyData;
import com.ASDC.backend.dto.RequestDTO.TaskDTORequest;
import com.ASDC.backend.dto.ResponseDTO.TaskDTOResponse;
import com.ASDC.backend.entity.*;
import com.ASDC.backend.exception.CustomException;
import com.ASDC.backend.mapper.TaskMapper;
import com.ASDC.backend.repository.TaskRepository;
import com.ASDC.backend.service.implementation.TaskServiceImpl;
import com.ASDC.backend.util.UtilFunctions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @Mock
    private UtilFunctions utilFunctions;

    @Mock
    private TaskMapper taskMapper;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private TaskServiceImpl taskService;

    private DummyData dummyData;
    private User user;
    private Room room;
    private UserRoomMapping userRoomMapping;
    private Task task;
    private TaskDTORequest taskDTORequest;
    private TaskDTOResponse taskDTOResponse;

    @BeforeEach
    void setUp() {
        dummyData = new DummyData();
        user = dummyData.createUser();
        room = dummyData.createRoom();
        userRoomMapping = dummyData.createUserRoomMapping();
        task = dummyData.createTask();
        taskDTORequest = dummyData.createTaskRequestDTO();
        taskDTOResponse = dummyData.createTaskDTO();

        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("test@example.com");
    }

//    @Test
//    void createTask_Success() {
//        when(utilFunctions.getUser(anyString())).thenReturn(user);
//        when(utilFunctions.getUserRoomMapping(user)).thenReturn(userRoomMapping);
//        when(utilFunctions.getRoom(userRoomMapping)).thenReturn(room);
//        when(utilFunctions.isTaskPresent(anyString())).thenReturn(false);
//        when(utilFunctions.getUserByName(anyString(), anyString())).thenReturn(user);
//        when(taskMapper.toTask(any(TaskDTORequest.class))).thenReturn(task);
//        when(taskRepository.save(any(Task.class))).thenReturn(task);
//        when(taskMapper.toTaskDTOResponse(any(Task.class))).thenReturn(taskDTOResponse);
//
//        TaskDTOResponse result = taskService.createTask(taskDTORequest);
//
//        assertNotNull(result);
//        assertEquals(taskDTOResponse, result);
//        verify(taskRepository, times(1)).save(any(Task.class));
//    }

    @Test
    void createTask_TaskAlreadyExists() {
        when(utilFunctions.getUser(anyString())).thenReturn(user);
        when(utilFunctions.getUserRoomMapping(user)).thenReturn(userRoomMapping);
        when(utilFunctions.getRoom(userRoomMapping)).thenReturn(room);
        when(utilFunctions.isTaskPresent(anyString())).thenReturn(true);

        assertThrows(CustomException.class, () -> taskService.createTask(taskDTORequest));
    }

//    @Test
//    void createTask_AssignedPersonNotFound() {
//        when(utilFunctions.getUser(anyString())).thenReturn(user);
//        when(utilFunctions.getUserRoomMapping(user)).thenReturn(userRoomMapping);
//        when(utilFunctions.getRoom(userRoomMapping)).thenReturn(room);
//        when(utilFunctions.isTaskPresent(anyString())).thenReturn(false);
//        when(utilFunctions.getUserByName(anyString(), anyString())).thenReturn(null);
//
//        assertThrows(CustomException.class, () -> taskService.createTask(taskDTORequest));
//    }

    @Test
    void getTask_Success() {
        when(utilFunctions.getUser(anyString())).thenReturn(user);
        when(utilFunctions.getUserRoomMapping(user)).thenReturn(userRoomMapping);
        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(task));
        when(taskMapper.toTaskDTOResponse(any(Task.class))).thenReturn(taskDTOResponse);

        TaskDTOResponse result = taskService.getTask(1);

        assertNotNull(result);
        assertEquals(taskDTOResponse, result);
    }

    @Test
    void getTask_TaskNotFound() {
        when(utilFunctions.getUser(anyString())).thenReturn(user);
        when(utilFunctions.getUserRoomMapping(user)).thenReturn(userRoomMapping);
        when(taskRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(CustomException.class, () -> taskService.getTask(1));
    }

    @Test
    void getAllTasks_Success() {
        when(utilFunctions.getUser(anyString())).thenReturn(user);
        when(utilFunctions.getUserRoomMapping(user)).thenReturn(userRoomMapping);
        when(utilFunctions.getRoom(userRoomMapping)).thenReturn(room);
        when(taskRepository.findAllByRoomID(anyInt())).thenReturn(Optional.of(List.of(task)));
        when(taskMapper.toTaskDTOResponse(any(Task.class))).thenReturn(taskDTOResponse);

        List<TaskDTOResponse> result = taskService.getAllTasks();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(taskDTOResponse, result.get(0));
    }

    @Test
    void getAllTasks_NoTasksFound() {
        when(utilFunctions.getUser(anyString())).thenReturn(user);
        when(utilFunctions.getUserRoomMapping(user)).thenReturn(userRoomMapping);
        when(utilFunctions.getRoom(userRoomMapping)).thenReturn(room);
        when(taskRepository.findAllByRoomID(anyInt())).thenReturn(Optional.empty());

        assertThrows(CustomException.class, () -> taskService.getAllTasks());
    }

    @Test
    void updateTask_Success() {
        when(utilFunctions.getUser(anyString())).thenReturn(user);
        when(utilFunctions.getUserRoomMapping(user)).thenReturn(userRoomMapping);
        when(taskRepository.findByName(anyString())).thenReturn(Optional.of(task));
        when(utilFunctions.getUserByName(anyString(), anyString())).thenReturn(user);
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        when(taskMapper.toTaskDTOResponse(any(Task.class))).thenReturn(taskDTOResponse);

        TaskDTOResponse result = taskService.updateTask(taskDTORequest);

        assertNotNull(result);
        assertEquals(taskDTOResponse, result);
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void updateTask_TaskNotFound() {
        when(utilFunctions.getUser(anyString())).thenReturn(user);
        when(utilFunctions.getUserRoomMapping(user)).thenReturn(userRoomMapping);
        when(taskRepository.findByName(anyString())).thenReturn(Optional.empty());

        assertThrows(CustomException.class, () -> taskService.updateTask(taskDTORequest));
    }

    @Test
    void updateTask_AssignedPersonNotFound() {
        when(utilFunctions.getUser(anyString())).thenReturn(user);
        when(utilFunctions.getUserRoomMapping(user)).thenReturn(userRoomMapping);
        when(taskRepository.findByName(anyString())).thenReturn(Optional.of(task));
        when(utilFunctions.getUserByName(anyString(), anyString())).thenReturn(null);

        assertThrows(CustomException.class, () -> taskService.updateTask(taskDTORequest));
    }

    @Test
    void updateTaskFinished_Success() {
        when(utilFunctions.getUser(anyString())).thenReturn(user);
        when(utilFunctions.getUserRoomMapping(user)).thenReturn(userRoomMapping);
        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        boolean result = taskService.updateTaskFinished(1);

        assertTrue(result);
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void updateTaskFinished_TaskNotFound() {
        when(utilFunctions.getUser(anyString())).thenReturn(user);
        when(utilFunctions.getUserRoomMapping(user)).thenReturn(userRoomMapping);
        when(taskRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(CustomException.class, () -> taskService.updateTaskFinished(1));
    }

    @Test
    void deleteTask_Success() {
        when(utilFunctions.getUser(anyString())).thenReturn(user);
        when(utilFunctions.getUserRoomMapping(user)).thenReturn(userRoomMapping);
        when(utilFunctions.getRoom(userRoomMapping)).thenReturn(room);
        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(task));

        boolean result = taskService.deleteTask(1);

        assertTrue(result);
        verify(taskRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void deleteTask_TaskNotFound() {
        when(utilFunctions.getUser(anyString())).thenReturn(user);
        when(utilFunctions.getUserRoomMapping(user)).thenReturn(userRoomMapping);
        when(utilFunctions.getRoom(userRoomMapping)).thenReturn(room);
        when(taskRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(CustomException.class, () -> taskService.deleteTask(1));
    }
}