package com.ASDC.backend.service.implementation;

import com.ASDC.backend.entity.Task;
import com.ASDC.backend.entity.User;
import com.ASDC.backend.repository.TaskRepository;
import com.ASDC.backend.repository.UserRepository;
import com.ASDC.backend.service.Interface.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of the EmailService interface.
 */
@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);
    private final JavaMailSender mailSender;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Sends a password reset email to the specified email address.
     *
     * @param emailTo the recipient's email address
     * @param token   the password reset token
     */
    @Override
    public void sendPasswordResetEmail(String emailTo, String token) {
        logger.info("Sending password reset email to: {}", emailTo);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailTo);
        message.setSubject("[RoomM8] Password Reset E-Mail");

        String body = "Please click the link below to reset your password:\n" +
                "http://localhost:3000/users/password/reset/request?token=" + token + "\n\n" +
                "If you did not request a password reset, please ignore this email.";
        message.setText(body);

        mailSender.send(message);
        logger.info("Password reset email sent to: {}", emailTo);
    }

    /**
     * Sends an email with the tasks due tomorrow to the specified user.
     *
     * @param user  the recipient user
     * @param tasks the list of tasks due tomorrow
     */
    @Override
    public void sendTaskEmails(User user, List<Task> tasks) {
        logger.info("Sending task email to: {}", user.getEmail());
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("[RoomM8] Tasks for tomorrow");

        StringBuilder body = new StringBuilder();
        body.append("Dear ").append(user.getFirstName()).append(",\n\n");
        body.append("You have the following tasks due tomorrow:\n");

        for (Task task : tasks) {
            body.append("- ").append(task.getName()).append("\n");
        }
        message.setText(body.toString());
        mailSender.send(message);
        logger.info("Task email sent to: {}", user.getEmail());
    }
}