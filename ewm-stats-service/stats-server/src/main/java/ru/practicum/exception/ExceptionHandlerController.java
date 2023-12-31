package ru.practicum.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
@ResponseBody
public class ExceptionHandlerController {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleInvalidDatesException(InvalidDatesException e) {
        return new ApiError("FORBIDDEN", "For the requested operation the conditions are not met.",
                e.getMessage(), LocalDateTime.now());
    }

    @Getter
    static class ApiError {
        private final String status;
        private final String reason;
        private final String message;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private final LocalDateTime timestamp;

        public ApiError(String status, String reason, String message, LocalDateTime timestamp) {
            this.status = status;
            this.reason = reason;
            this.message = message;
            this.timestamp = timestamp;
        }
    }
}
