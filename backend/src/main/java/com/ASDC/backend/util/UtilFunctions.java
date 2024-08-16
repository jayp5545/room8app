package com.ASDC.backend.util;

import com.ASDC.backend.entity.*;
import com.ASDC.backend.exception.CustomException;
import com.ASDC.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UtilFunctions {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserRoomRepository userRoomRepository;

    @Autowired
    private GroceryListRepository groceryListRepository;

    @Autowired
    private GroceryItemRepository groceryItemRepository;

    @Autowired
    private TaskRepository taskRepository;

    public User getUser(String email){
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isEmpty() || !optionalUser.isPresent()){
            throw  new CustomException("400", "User doesn't Exist!");
        }

        return optionalUser.get();
    }

    public UserRoomMapping getUserRoomMapping(User user){
        Optional<UserRoomMapping> userRoom = userRoomRepository.findByUserID(user.getId());

        if (userRoom.isEmpty() || !userRoom.isPresent()){
            throw new CustomException("400","User are not belong to any room!");
        }

        return userRoom.get();
    }

    public Room getRoom(UserRoomMapping userRoom){
        Optional<Room> room = roomRepository.findById(userRoom.getRoomid().getId());

        if (room.isEmpty() || !room.isPresent()){
            throw new CustomException("400","Room Data Not Fount!");
        }

        return room.get();
    }

    public boolean isTaskPresent(String taskName){
        Optional<Task> task = taskRepository.findByName(taskName);
        if (task.isEmpty() || !task.isPresent()){
            return false;
        }
        return true;
    }

    public User getUserByName(String firstName, String lastName) {
        Optional<User> user = userRepository.findByName(firstName, lastName);
        return user.orElse(null);
    }

    public User getCurrentUser(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return getUser(email);
    }
}
