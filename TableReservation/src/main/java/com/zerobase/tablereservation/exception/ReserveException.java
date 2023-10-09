package com.zerobase.tablereservation.exception;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReserveException extends RuntimeException{
    private ErrorCode errorCode;
    private String errorMessage;

    public ReserveException(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getDescription();
    }
}
