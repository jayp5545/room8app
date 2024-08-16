package com.ASDC.backend.controller;

import com.ASDC.backend.dto.ForgotPasswordDTO;
import com.ASDC.backend.dto.ResetPasswordDTO;
import com.ASDC.backend.service.implementation.ForgotPasswordServiceImpl;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/users")
public class ForgotPasswordController
{
    private ForgotPasswordServiceImpl forgotPasswordServiceImpl;

    public ForgotPasswordController(ForgotPasswordServiceImpl forgotPasswordServiceImpl)
    {
        this.forgotPasswordServiceImpl = forgotPasswordServiceImpl;
    }
    @PostMapping("/password/forgot")
    public ResponseEntity<?> forgotPassword(@RequestBody(required = true) ForgotPasswordDTO forgotPasswordDTO)
    {
        String email = forgotPasswordDTO.getEmail();
        boolean validRequest = forgotPasswordServiceImpl.forgotPassword(email);

        // Regardless the mail is found or not, return OK status code.
        ResponseEntity requestServed = ResponseEntity.status(HttpStatus.OK).build();
        return requestServed;
    }

    @Transactional
    @PostMapping("/password/reset")
    public ResponseEntity<?> resetPassword(@RequestBody(required = true) ResetPasswordDTO resetPasswordDTO)
    {
        boolean validRequest = forgotPasswordServiceImpl.resetPassword(resetPasswordDTO);
        if(!validRequest)
        {
            ResponseEntity invalidReset = ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            return invalidReset;
        }

        ResponseEntity validReset = ResponseEntity.status(HttpStatus.OK).build();
        return validReset;
    }
}
