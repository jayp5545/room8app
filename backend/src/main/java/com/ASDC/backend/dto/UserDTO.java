package com.ASDC.backend.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
