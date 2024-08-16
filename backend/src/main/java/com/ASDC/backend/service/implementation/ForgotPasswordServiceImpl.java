package com.ASDC.backend.service.implementation;

import com.ASDC.backend.dto.ResetPasswordDTO;
import com.ASDC.backend.entity.PasswordResetToken;
import com.ASDC.backend.entity.User;
import com.ASDC.backend.repository.PasswordResetTokenRepository;
import com.ASDC.backend.repository.UserRepository;
import com.ASDC.backend.service.Interface.ForgotPasswordService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ForgotPasswordServiceImpl implements ForgotPasswordService {

    private UserRepository userRepository;
    private PasswordResetTokenRepository passwordResetTokenRepository;
    private PasswordEncoder passwordEncoder;
    private EmailServiceImpl emailServiceImpl;
    private TokenServiceImpl tokenServiceImpl;

    public ForgotPasswordServiceImpl(UserRepository userRepository, PasswordResetTokenRepository passwordResetTokenRepository, EmailServiceImpl emailServiceImpl, TokenServiceImpl tokenServiceImpl, PasswordEncoder passwordEncoder)
    {
        this.userRepository = userRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.emailServiceImpl = emailServiceImpl;
        this.tokenServiceImpl = tokenServiceImpl;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean forgotPassword(String email)
    {
        Optional<User> userOptional = userRepository.findByEmail(email);

        boolean userNotFound = userOptional.isEmpty();
        if(userNotFound)
        {
            return false;
        }

        User user = userOptional.get();

        String token = tokenServiceImpl.generateToken();

        tokenServiceImpl.saveToken(token, user);
        emailServiceImpl.sendPasswordResetEmail(email, token);
        return true;
    }

    @Override
    public boolean resetPassword(ResetPasswordDTO resetPasswordDTO)
    {
        boolean passwordsMatch = matchPasswords(resetPasswordDTO);
        if(!passwordsMatch)
        {
            return false;
        }

        String token = resetPasswordDTO.getToken();
        boolean validToken = tokenServiceImpl.validToken(token);
        if(!validToken)
        {
            return false;
        }

        // Update User's password
        String passwordToUpdate = resetPasswordDTO.getPassword();
        User user = passwordResetTokenRepository.findByToken(token).get().getUser();
        user.setPassword(passwordEncoder.encode(passwordToUpdate));
        userRepository.save(user);

        // Delete token from table
        PasswordResetToken tokenToDelete = passwordResetTokenRepository.findByToken(token).get();
        passwordResetTokenRepository.delete(tokenToDelete);

        return true;
    }

    @Override
    public boolean matchPasswords(ResetPasswordDTO resetPasswordDTO)
    {
        String password = resetPasswordDTO.getPassword();
        String confirmPassword = resetPasswordDTO.getConfirmPassword();
        boolean passwordsMatch = password.equals(confirmPassword);
        return passwordsMatch;
    }

}
