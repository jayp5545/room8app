package com.ASDC.backend.service.Interface;

import com.ASDC.backend.dto.ResetPasswordDTO;

public interface ForgotPasswordService {

    boolean forgotPassword(String email);
    boolean resetPassword(ResetPasswordDTO resetPasswordDTO);
    boolean matchPasswords(ResetPasswordDTO resetPasswordDTO);
}
