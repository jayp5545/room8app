package com.ASDC.backend.exception;

import lombok.*;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Component
public class CustomExceptionResponse {

    private String errorCode;
    private String errorMessage;

}
