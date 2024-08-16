package com.ASDC.backend.service.Interface;

import com.ASDC.backend.entity.Task;
import com.ASDC.backend.entity.User;

import java.util.List;

public interface EmailService {

    void sendPasswordResetEmail(String emailTo, String token);
    void sendTaskEmails(User user, List<Task> tasks);
}
