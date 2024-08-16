package com.ASDC.backend.dto.RequestDTO;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTORequest {

    @NotBlank(message = "User first name can't be Null or Empty!")
    private String firstName;

    @NotBlank(message = "User last name can't be Null or Empty!")
    private String lastName;

    @NotBlank(message = "User email can't be Null or Empty!")
    private String email;

    @NotBlank(message = "User password can't be Null or Empty!")
    private String password;
}
