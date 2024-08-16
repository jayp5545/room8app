package com.ASDC.backend.service;

import com.ASDC.backend.entity.Task;
import com.ASDC.backend.entity.User;
import com.ASDC.backend.repository.TaskRepository;
import com.ASDC.backend.repository.UserRepository;
import com.ASDC.backend.service.AlertEmailService.TaskAlertService;
import com.ASDC.backend.service.implementation.EmailServiceImpl;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskAlertServiceTest {
    @Mock
    UserRepository userRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private EmailServiceImpl emailServiceImpl;

    @InjectMocks
    private TaskAlertService taskAlertService;

    private User user;
    private Task task;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setEmail("demo@demo.com");

        task = new Task();
        task.setName("Test Task");

        when(userRepository.findAll()).thenReturn(List.of(user));
        when(taskRepository.findByAssignedTo(user.getEmail())).thenReturn(List.of(task));
    }

    @Test
    public void taskAlertWithTasksDueTomorrowTest() throws MessagingException{
        task.setTaskDate(LocalDate.now().plusDays(1));
        taskAlertService.taskAlert();

        verify(userRepository,times(1)).findAll();
        verify(taskRepository,times(1)).findByAssignedTo("demo@demo.com");
        verify(emailServiceImpl,times(1)).sendTaskEmails(user,List.of(task));

    }

    @Test
    public void taskAlertTestWithNoTaskDueTomorrowTest() throws MessagingException {
        when(userRepository.findAll()).thenReturn(List.of(user));
        when(taskRepository.findByAssignedTo(user.getEmail())).thenReturn(List.of(task));
        task.setTaskDate(LocalDate.now().plusDays(2));

        taskAlertService.taskAlert();

        verify(userRepository,times(1)).findAll();
        verify(taskRepository,times(1)).findByAssignedTo(user.getEmail());
        verify(emailServiceImpl,times(0)).sendTaskEmails(any(User.class),anyList());

    }
}
