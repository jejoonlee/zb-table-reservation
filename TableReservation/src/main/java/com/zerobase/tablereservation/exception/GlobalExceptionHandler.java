package com.zerobase.tablereservation.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

import static com.zerobase.tablereservation.exception.ErrorCode.INTERNAL_SERVER_ERROR;
import static com.zerobase.tablereservation.exception.ErrorCode.INVALID_REQUEST;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MemberException.class)
    public ErrorResponse handleMemberException(MemberException e) {
        log.error("{} has occurred.", e.getErrorCode());

        return new ErrorResponse(HttpStatus.BAD_REQUEST, e.getErrorCode(), e.getErrorMessage());
    }

    @ExceptionHandler(KioskException.class)
    public ErrorResponse handleKioskException(KioskException e) {
        log.error("{} has occurred.", e.getErrorCode());

        return new ErrorResponse(HttpStatus.BAD_REQUEST, e.getErrorCode(), e.getErrorMessage());
    }

    @ExceptionHandler(ReserveException.class)
    public ErrorResponse handleReserveException(ReserveException e) {
        log.error("{} has occurred.", e.getErrorCode());

        return new ErrorResponse(HttpStatus.BAD_REQUEST, e.getErrorCode(), e.getErrorMessage());
    }

    @ExceptionHandler(StoreException.class)
    public ErrorResponse handleStoreException(StoreException e) {
        log.error("{} has occurred.", e.getErrorCode());

        return new ErrorResponse(HttpStatus.BAD_REQUEST, e.getErrorCode(), e.getErrorMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("MethodArgumentNotValidException : " + e.getMessage());

        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ErrorResponse handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        log.error("DataIntegrityViolationException has occurred : " + e.getMessage(), e);

        return new ErrorResponse(HttpStatus.BAD_REQUEST, INVALID_REQUEST, INVALID_REQUEST.getDescription());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ErrorResponse handleAccessDeniedException(AccessDeniedException e) {
        log.error("AccessDeniedException has occurred: " + e.getMessage(), e);

        return new ErrorResponse(HttpStatus.UNAUTHORIZED,
                ErrorCode.USER_PERMISSION_NOT_GRANTED,
                ErrorCode.USER_PERMISSION_NOT_GRANTED.getDescription());
    }

    @ExceptionHandler(NullPointerException.class)
    public ErrorResponse handleNullPointerException(NullPointerException e) {
        log.error("NullPointerException has occurred : " + e.getMessage());

        return new ErrorResponse(HttpStatus.BAD_REQUEST,
                ErrorCode.NO_INPUT,
                ErrorCode.NO_INPUT.getDescription());
    }

    @ExceptionHandler(DateTimeParseException.class)
    public ErrorResponse handleDateTimeParseException(DateTimeParseException e) {
        log.error("DateTimeParseException has occurred : " + e.getMessage());

        return new ErrorResponse(HttpStatus.BAD_REQUEST,
                ErrorCode.WRONG_DATE_INPUT,
                ErrorCode.WRONG_DATE_INPUT.getDescription() + " " + e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ErrorResponse handleException(Exception e) {
        log.error("Exception has occurred: " + e.getMessage(), e);

        if (e.getMessage().split(":")[0].equals("JSON parse error")) {
            log.error(e.getMessage());

            return new ErrorResponse(HttpStatus.BAD_REQUEST,
                    ErrorCode.WRONG_INPUT_REQUEST,
                    ErrorCode.WRONG_INPUT_REQUEST.getDescription());
        }

        return new ErrorResponse(HttpStatus.BAD_REQUEST,
                INTERNAL_SERVER_ERROR,
                INTERNAL_SERVER_ERROR.getDescription());
    }

}
