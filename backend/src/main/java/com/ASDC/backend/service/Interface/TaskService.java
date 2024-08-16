package com.ASDC.backend.service.Interface;

import com.ASDC.backend.dto.RequestDTO.TaskDTORequest;
import com.ASDC.backend.dto.ResponseDTO.TaskDTOResponse;

import java.util.List;

public interface TaskService {
    TaskDTOResponse createTask(TaskDTORequest taskDTORequest);

    TaskDTOResponse getTask(int taskID);

    List<TaskDTOResponse> getAllTasks();

    TaskDTOResponse updateTask(TaskDTORequest taskDTORequest);

    boolean updateTaskFinished(int taskID);

    boolean deleteTask(int taskID);
}
