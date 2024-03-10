package edu.java.bot.errors;


import org.apache.commons.lang3.exception.ExceptionUtils;
import org.example.dto.response.ApiErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionApiHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> validationException(MethodArgumentNotValidException exception) {
        String stackTrace = ExceptionUtils.getStackTrace(exception);
        var response = new ApiErrorResponse(
            "Validation Error",
            "400", exception.getClass().getSimpleName(),
            exception.getMessage(), new String[] {stackTrace}

        );

        return ResponseEntity.badRequest().body(response);
    }
}
