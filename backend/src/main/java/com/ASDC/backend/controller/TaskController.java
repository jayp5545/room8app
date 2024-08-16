package com.ASDC.backend.controller;
import com.ASDC.backend.dto.RequestDTO.TaskDTORequest;
import com.ASDC.backend.dto.ResponseDTO.TaskDTOResponse;
import com.ASDC.backend.exception.CustomException;
import com.ASDC.backend.service.implementation.TaskServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/task")
public class TaskController {

    @Autowired
    private TaskServiceImpl taskService;

    @PostMapping("/add")
    public ResponseEntity<TaskDTOResponse> createTask(@RequestBody TaskDTORequest taskDTO){
        System.out.println(taskDTO);
        System.out.println(taskDTO.getName());
        System.out.println(taskDTO.getDescription());

        if (taskDTO.getName() == null){
            throw new CustomException("400", "Task Name can't be Null!");
        }

        if (taskDTO.getName().isBlank()){
            throw new CustomException("400", "Task Name can't be Empty!");
        }

        if (taskDTO.getDescription() == null){
            throw new CustomException("400", "Task description can't be Null!");
        }

        if (taskDTO.getDescription().isBlank()){
            throw new CustomException("400", "Task description can't be Empty!");
        }

        if (taskDTO.getAssignedTO() == null){
            System.out.println(taskDTO.getAssignedTO());
            throw new CustomException("400", "Task assigned person can't be Null!");
        }

        if (taskDTO.getAssignedTO().isBlank()){
            throw new CustomException("400", "Task assigned person can't be Empty!");
        }

        if (taskDTO.getTaskDate() == null){
            throw new CustomException("400", "Task date can't be Null!");
        }

        if (taskDTO.getTaskDate().isBefore(LocalDate.now())){
            throw new CustomException("400", "Task Date must not be before today's date!");
        }

        TaskDTOResponse response = taskService.createTask(taskDTO);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/get")
    public  ResponseEntity<TaskDTOResponse> getTask(@RequestParam(name = "id", required = true) int taskID)
    {
        if (taskID < 0){
            throw new CustomException("400", "Task ID can't be negative!");
        }

        TaskDTOResponse response = taskService.getTask(taskID);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/get/all")
    public ResponseEntity<List<TaskDTOResponse>> getAllTask(){

        List<TaskDTOResponse> response = taskService.getAllTasks();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<TaskDTOResponse> updateTask(@RequestBody TaskDTORequest taskDTORequest){

        if (taskDTORequest.getName() == null){
            throw new CustomException("400", "Task Name can't be Null!");
        }

        if (taskDTORequest.getName().isBlank()){
            throw new CustomException("400", "Task Name can't be Empty!");
        }

        if (taskDTORequest.getDescription() == null){
            throw new CustomException("400", "Task description can't be Null!");
        }

        if (taskDTORequest.getDescription().isBlank()){
            throw new CustomException("400", "Task description can't be Empty!");
        }

        if (taskDTORequest.getAssignedTO() == null){
            throw new CustomException("400", "Task assigned person can't be Null!");
        }

        if (taskDTORequest.getAssignedTO().isBlank()){
            throw new CustomException("400", "Task assigned person can't be Empty!");
        }

        if (taskDTORequest.getTaskDate() == null){
            throw new CustomException("400", "Task date can't be Null!");
        }

        if (taskDTORequest.getTaskDate().isBefore(LocalDate.now())){
            throw new CustomException("400", "Task Date must not be before today's date!");
        }

        TaskDTOResponse response = taskService.updateTask(taskDTORequest);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/update/finished")
    public ResponseEntity<Boolean> updateFinished(@RequestParam(name = "id", required = true) int taskID)
    {
        if (taskID < 0){
            throw new CustomException("400", "Task ID can't be negative!");
        }

        boolean response = taskService.updateTaskFinished(taskID);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @DeleteMapping("/delete")
    public ResponseEntity<Boolean> deleteTask(@RequestParam(name = "id", required = true) int taskID){

        if (taskID < 0){
            throw new CustomException("400", "Task ID can't be negative!");
        }

        boolean response = taskService.deleteTask(taskID);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
