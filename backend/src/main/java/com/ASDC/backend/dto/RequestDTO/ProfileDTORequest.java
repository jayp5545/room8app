package com.ASDC.backend.dto.RequestDTO;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileDTORequest {
    private String email;
    private String firstName;
    private String lastName;
}
