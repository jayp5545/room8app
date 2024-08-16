package com.ASDC.backend.mapper;


import com.ASDC.backend.dto.RequestDTO.TaskDTORequest;
import com.ASDC.backend.dto.ResponseDTO.TaskDTOResponse;
import com.ASDC.backend.entity.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    @Autowired
    private RoomMapper roomMapper;

    @Autowired
    private UserMapper userMapper;

    public Task toTask(TaskDTORequest taskDTORequest) {
        if (taskDTORequest == null) {
            return null;
        }

        Task task = new Task();
        task.setName(taskDTORequest.getName());
        task.setTaskDate(taskDTORequest.getTaskDate());
        task.setDescription(taskDTORequest.getDescription());

        return task;
    }

    public TaskDTOResponse toTaskDTOResponse(Task task) {
        if (task == null) {
            return null;
        }

        TaskDTOResponse taskDTOResponse = new TaskDTOResponse();
        taskDTOResponse.setId(task.getId());
        taskDTOResponse.setRoom(roomMapper.toRoomDTOResponse(task.getRoom()));
        taskDTOResponse.setName(task.getName());
        taskDTOResponse.setCreated_by(userMapper.toUserDTOResponse(task.getCreated_by()));
        taskDTOResponse.setLast_modified_by(userMapper.toUserDTOResponse(task.getLast_modified_by()));
        taskDTOResponse.setDate_of_creation(task.getDate_of_creation());
        taskDTOResponse.setLast_modified_On(task.getLast_modified_on());
        taskDTOResponse.setTaskDate(task.getTaskDate());
        taskDTOResponse.setDescription(task.getDescription());
        taskDTOResponse.setAssignedTo(userMapper.toUserDTOResponse(task.getAssigned_to()));
        taskDTOResponse.setFinished(task.isFinished());

        return taskDTOResponse;
    }

}
