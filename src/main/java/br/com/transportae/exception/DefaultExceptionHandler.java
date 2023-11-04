package br.com.transportae.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class DefaultExceptionHandler {

    @ExceptionHandler({Exception.class, RuntimeException.class})
    public ResponseEntity<ApiErrorDTO> handleException(Exception exception, HttpServletRequest request) {
        ApiErrorDTO ApiErrorDTO = new ApiErrorDTO(
            request.getRequestURI(),
            exception.getMessage(),
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            LocalDateTime.now()
        );

        return new ResponseEntity<ApiErrorDTO>(ApiErrorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
