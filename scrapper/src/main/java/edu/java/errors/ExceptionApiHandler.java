package edu.java.errors;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.example.dto.response.ApiErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

@RestControllerAdvice
public class ExceptionApiHandler {
    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ApiErrorResponse> validationException(HandlerMethodValidationException exception) {
        String stackTrace = ExceptionUtils.getStackTrace(exception);
        var response = new ApiErrorResponse(
            "Validation Error",
            "400", exception.getClass().getSimpleName(),
            exception.getMessage(), new String[] {stackTrace}

        );

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiErrorResponse> validationException(NotFoundException exception) {
        String stackTrace = ExceptionUtils.getStackTrace(exception);
        var response = new ApiErrorResponse(
            "Not Found",
            "404", exception.getClass().getSimpleName(),
            exception.getMessage(), new String[] {stackTrace}

        );

        return ResponseEntity.badRequest().body(response);
    }
}
