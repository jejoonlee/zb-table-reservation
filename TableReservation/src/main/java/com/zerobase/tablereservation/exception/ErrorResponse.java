package com.zerobase.tablereservation.exception;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorResponse {
    private HttpStatus httpStatus;
    private ErrorCode errorCode;
    private String errorMessage;
}
