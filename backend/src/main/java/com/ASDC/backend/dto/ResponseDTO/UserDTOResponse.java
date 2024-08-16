package com.ASDC.backend.dto.ResponseDTO;


import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTOResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
}
