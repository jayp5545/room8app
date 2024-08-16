package com.ASDC.backend.service.implementation;

import com.ASDC.backend.controller.ExpenseController;
import com.ASDC.backend.dto.RequestDTO.TaskDTORequest;
import com.ASDC.backend.dto.ResponseDTO.TaskDTOResponse;
import com.ASDC.backend.entity.*;
import com.ASDC.backend.exception.CustomException;
import com.ASDC.backend.mapper.TaskMapper;
import com.ASDC.backend.repository.TaskRepository;
import com.ASDC.backend.service.Interface.TaskService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ASDC.backend.util.UtilFunctions;
import com.ASDC.backend.service.Interface.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class TaskServiceImpl implements TaskService {

    private static final Logger logger = LogManager.getLogger(ExpenseController.class);

    @Autowired
    UtilFunctions utilFunctions;

    @Autowired
    TaskMapper taskMapper;

    @Autowired
    UserService userService;

    @Autowired
    TaskRepository taskRepository;

    @Override
    public TaskDTOResponse createTask(TaskDTORequest taskDTORequest) {
        logger.info("Adding task");

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = utilFunctions.getUser(email);
        UserRoomMapping userRoom = utilFunctions.getUserRoomMapping(user);
        Room room = utilFunctions.getRoom(userRoom);

        if (utilFunctions.isTaskPresent(taskDTORequest.getName())){
            throw new CustomException("400","Task with the given name already exist!");
        }

        String[] assignedPersonName = taskDTORequest.getAssignedTO().split(" ");

        User assignedPersonEmail = userService.getUserByEmail(email);
//        User assignedPerson = utilFunctions.getUserByName(assignedPersonName[0], assignedPersonName[1]);

        logger.info("Just before ");

        Task task = taskMapper.toTask(taskDTORequest);
        task.setAssigned_to(assignedPersonEmail);
        task.setCreated_by(user);
        task.setDate_of_creation(LocalDateTime.now());
        task.setLast_modified_on(LocalDateTime.now());
        task.setLast_modified_by(user);
        task.setRoom(room);
        task.setFinished(false);

        System.out.println("Here: " + task);

        Task response = this.taskRepository.save(task);

        return taskMapper.toTaskDTOResponse(response);
    }

    @Override
    public TaskDTOResponse getTask(int taskID) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = utilFunctions.getUser(email);
        UserRoomMapping userRoom = utilFunctions.getUserRoomMapping(user);

        Optional<Task> response = taskRepository.findById((long) taskID);

        if (!response.isPresent()){
            throw new CustomException("400", "Task doesn't exist with this ID!");
        }

        return taskMapper.toTaskDTOResponse(response.get());
    }

    @Override
    public List<TaskDTOResponse> getAllTasks() {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = utilFunctions.getUser(email);
        UserRoomMapping userRoom = utilFunctions.getUserRoomMapping(user);

        Room room = utilFunctions.getRoom(userRoom);

        Optional<List<Task>> optionalTask = taskRepository.findAllByRoomID(room.getId());

        if (!optionalTask.isPresent()) {
            throw new CustomException("400", "Something went wrong while fetching data!");
        }

        List<TaskDTOResponse> response = new ArrayList<>();

        for (Task task : optionalTask.get()){
            response.add(taskMapper.toTaskDTOResponse(task));
        }

        return response;
    }

    @Override
    public TaskDTOResponse updateTask(TaskDTORequest taskDTORequest) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = utilFunctions.getUser(email);
        UserRoomMapping userRoom = utilFunctions.getUserRoomMapping(user);

        Optional<Task> actualTask = taskRepository.findByName(taskDTORequest.getName());

        if (!actualTask.isPresent()){
            throw new CustomException("400", "Task with given name doesn't exist!");
        }

        String[] assignedPersonName = taskDTORequest.getAssignedTO().split(" ");

        User assignedPerson = utilFunctions.getUserByName(assignedPersonName[0], assignedPersonName[1]);

        if (assignedPerson == null){
            throw new CustomException("400","The person to which this task is assigned does not exist!");
        }

        Task task = actualTask.get();
        task.setName(taskDTORequest.getName());
        task.setTaskDate(taskDTORequest.getTaskDate());
        task.setAssigned_to(assignedPerson);
        task.setDescription(taskDTORequest.getDescription());
        task.setLast_modified_on(LocalDateTime.now());
        task.setLast_modified_by(user);

        Task response = this.taskRepository.save(task);

        return taskMapper.toTaskDTOResponse(response);
    }

    @Override
    public boolean updateTaskFinished(int taskID) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = utilFunctions.getUser(email);
        UserRoomMapping userRoom = utilFunctions.getUserRoomMapping(user);

        Optional<Task> task = taskRepository.findById((long) taskID);

        if (!task.isPresent()) {
            throw new CustomException("400", "Task with given ID doesn't Exist!");
        }

        Task updateTask = task.get();
        updateTask.setFinished(true);
        updateTask.setLast_modified_on(LocalDateTime.now());
        updateTask.setLast_modified_by(user);

        Task response = taskRepository.save(updateTask);

        return true;
    }

    @Override
    public boolean deleteTask(int taskID) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = utilFunctions.getUser(email);
        UserRoomMapping userRoom = utilFunctions.getUserRoomMapping(user);
        Room room = utilFunctions.getRoom(userRoom);

        Optional<Task> task = taskRepository.findById((long) taskID);

        if (!task.isPresent()) {
            throw new CustomException("400", "Task with given ID doesn't Exist!");
        }

        taskRepository.deleteById((long) taskID);

        return true;
    }
}
