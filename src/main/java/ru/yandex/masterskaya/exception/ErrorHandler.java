package ru.yandex.masterskaya.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.masterskaya.exception.model.ApiError;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFoundException(EntityNotFoundException e) {
        return ApiError.builder()
                .status(HttpStatus.NOT_FOUND.toString())
                .reason("The required object was not found.")
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleConflictException(ConflictException e) {
        return ApiError.builder()
                .status(HttpStatus.CONFLICT.toString())
                .reason("For the requested operation the conditions are not met.")
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleUnhandled(Exception e) {
        return ApiError.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                .reason("Internal server error.")
                .message(e.getClass() + " - " + e.getMessage())
                .errors(Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.toList()))
                .timestamp(LocalDateTime.now())
                .build();
    }
}
