package com.ASDC.backend.service.AlertEmailService;

import com.ASDC.backend.entity.Task;
import com.ASDC.backend.entity.User;
import com.ASDC.backend.repository.TaskRepository;
import com.ASDC.backend.repository.UserRepository;
import com.ASDC.backend.service.implementation.EmailServiceImpl;
import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Service for sending task alert emails.
 */
@Service
public class TaskAlertService {

    private static final Logger logger = LoggerFactory.getLogger(TaskAlertService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private EmailServiceImpl emailServiceImpl;

    /**
     * Scheduled task to send task alert emails to users at 9 PM every day.
     *
     * @throws MessagingException if there is an error sending the email
     */
    @Scheduled(cron = "0 0 21 * * *", zone = "America/Halifax")
    public void taskAlert() throws MessagingException {
        logger.info("Starting task alert job");
        List<User> users = userRepository.findAll();
        LocalDate tomorrowDate = LocalDate.now().plusDays(1);
        for (User user : users) {
            List<Task> tasks = taskRepository.findByAssignedTo(user.getEmail());

            List<Task> tasksDueTomorrow = tasks.stream()
                    .filter(task -> task.getTaskDate().isEqual(tomorrowDate))
                    .toList();
            if (!tasksDueTomorrow.isEmpty()) {
                logger.info("Sending task alert email to: {}", user.getEmail());
                emailServiceImpl.sendTaskEmails(user, tasksDueTomorrow);
            }
        }
        logger.info("Task alert job completed");
    }
}