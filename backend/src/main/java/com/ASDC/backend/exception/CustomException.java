package com.ASDC.backend.exception;

import lombok.*;
import org.springframework.stereotype.Component;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Component

public class CustomException extends RuntimeException{
    private String errorCode;
    private String errorMessage;
}
