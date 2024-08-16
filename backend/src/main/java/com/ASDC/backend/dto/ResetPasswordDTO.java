package com.ASDC.backend.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResetPasswordDTO {
    private String token;
    private String password;
    private String confirmPassword;
}
