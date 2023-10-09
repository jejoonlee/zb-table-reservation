package com.zerobase.tablereservation.exception;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KioskException extends RuntimeException{
    private ErrorCode errorCode;
    private String errorMessage;

    public KioskException(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getDescription();
    }
}