package com.ASDC.backend.service;

import com.ASDC.backend.DummyData.DummyData;
import com.ASDC.backend.entity.Task;
import com.ASDC.backend.entity.User;
import com.ASDC.backend.service.Interface.EmailService;
import com.ASDC.backend.service.implementation.EmailServiceImpl;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class EmailServiceImplTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailServiceImpl emailService;
//    @Mock
//    private EmailUtil emailUtil;

    @Test
    public void sendTaskEmailsTest() throws MessagingException {
        User user = new User();
        user.setEmail("demo@demo.com");
        user.setFirstName("Demo");

        Task task1 = new Task();
        task1.setName("Task 1");

        List<Task> tasks = List.of(task1);

        emailService.sendTaskEmails(user, tasks);

        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(messageCaptor.capture());

        SimpleMailMessage message = messageCaptor.getValue();
        assertEquals("demo@demo.com", message.getTo()[0]);
        assertEquals("[RoomM8] Tasks for tomorrow", message.getSubject());
        String expectedBody = "Dear Demo,\n\nYou have the following tasks due tomorrow:\n- Task 1\n";
        assertEquals(expectedBody, message.getText());
    }
    @Test
    void testSendPasswordResetEmail() {
        String email = "test@example.com";
        String token = "reset-token";

        // Call the method to test
        emailService.sendPasswordResetEmail(email, token);

        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }
}
