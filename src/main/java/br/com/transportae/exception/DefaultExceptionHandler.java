package br.com.transportae.exception;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class DefaultExceptionHandler {

    @ExceptionHandler({Exception.class, RuntimeException.class})
    public ResponseEntity<ApiErrorDTO> handleException(Exception exception, HttpServletRequest request) {
        ApiErrorDTO apiErrorDTO = new ApiErrorDTO(
            request.getRequestURI(),
            exception.getMessage(),
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            LocalDateTime.now()
        );

        return new ResponseEntity<ApiErrorDTO>(apiErrorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorDTO> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception, HttpServletRequest request) {
        String mensagem = "Um ou mais campos são inválidos";

        List<CampoInvalidoDTO> erros = exception.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(fieldError -> new CampoInvalidoDTO(
                    fieldError.getField(), 
                    fieldError.getDefaultMessage()
                )
            )
            .collect(Collectors.toList());
        
        ApiErrorDTO apiErrorDTO = new ApiErrorDTO(
            request.getRequestURI(),
            mensagem,
            HttpStatus.BAD_REQUEST.value(),
            LocalDateTime.now(),
            erros
        );

        return new ResponseEntity<ApiErrorDTO>(apiErrorDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiErrorDTO> handleEntityNotFoundException(EntityNotFoundException exception, HttpServletRequest request) {
        ApiErrorDTO apiErrorDTO = new ApiErrorDTO(
            request.getRequestURI(),
            exception.getMessage(),
            HttpStatus.NOT_FOUND.value(),
            LocalDateTime.now()
        );

        return new ResponseEntity<ApiErrorDTO>(apiErrorDTO, HttpStatus.NOT_FOUND);
    }
}
