package br.com.transportae.exception;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import br.com.transportae.usuario.exceptions.UsuarioExistenteException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class DefaultExceptionHandler {

    @ExceptionHandler({Exception.class, RuntimeException.class})
    public ResponseEntity<ApiErrorDto> handleException(Exception exception, HttpServletRequest request) {
        ApiErrorDto apiErrorDto = new ApiErrorDto(
            request.getRequestURI(),
            exception.getMessage(),
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            LocalDateTime.now()
        );

        return new ResponseEntity<ApiErrorDto>(apiErrorDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception, HttpServletRequest request) {
        String mensagem = "Um ou mais campos são inválidos";

        List<CampoInvalidoDto> erros = exception.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(fieldError -> new CampoInvalidoDto(
                    fieldError.getField(), 
                    fieldError.getDefaultMessage()
                )
            )
            .collect(Collectors.toList());
        
        ApiErrorDto apiErrorDto = new ApiErrorDto(
            request.getRequestURI(),
            mensagem,
            HttpStatus.BAD_REQUEST.value(),
            LocalDateTime.now(),
            erros
        );

        return new ResponseEntity<ApiErrorDto>(apiErrorDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiErrorDto> handleUsernameNotFoundException(UsernameNotFoundException exception, HttpServletRequest request) {
        ApiErrorDto apiErrorDto = new ApiErrorDto(
            request.getRequestURI(),
            exception.getMessage(),
            HttpStatus.NOT_FOUND.value(),
            LocalDateTime.now()
        );

        return new ResponseEntity<ApiErrorDto>(apiErrorDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<ApiErrorDto> handleSignatureException(SignatureException exception, HttpServletRequest request) {
        String mensagem = "Chave de acesso inválida! Por favor, efetue o login novamente";

        ApiErrorDto apiErrorDto = new ApiErrorDto(
            request.getRequestURI(),
            mensagem,
            HttpStatus.FORBIDDEN.value(),
            LocalDateTime.now()
        );

        return new ResponseEntity<ApiErrorDto>(apiErrorDto, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ApiErrorDto> handleExpiredJwtException(ExpiredJwtException exception, HttpServletRequest request) {
        String mensagem = "Tempo de acesso expirado! Por favor, efetue o login novamente";

        ApiErrorDto apiErrorDto = new ApiErrorDto(
            request.getRequestURI(),
            mensagem,
            HttpStatus.FORBIDDEN.value(),
            LocalDateTime.now()
        );

        return new ResponseEntity<ApiErrorDto>(apiErrorDto, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiErrorDto> handleBadCredentialsException(BadCredentialsException exception, HttpServletRequest request) {
        ApiErrorDto apiErrorDto = new ApiErrorDto(
            request.getRequestURI(),
            exception.getMessage(),
            HttpStatus.BAD_REQUEST.value(),
            LocalDateTime.now()
        );

        return new ResponseEntity<ApiErrorDto>(apiErrorDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiErrorDto> handleAccessDeniedException(AccessDeniedException exception, HttpServletRequest request) {
        ApiErrorDto apiErrorDto = new ApiErrorDto(
            request.getRequestURI(),
            exception.getMessage(),
            HttpStatus.FORBIDDEN.value(),
            LocalDateTime.now()
        );

        return new ResponseEntity<ApiErrorDto>(apiErrorDto, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiErrorDto> handleEntityNotFoundException(EntityNotFoundException exception, HttpServletRequest request) {
        ApiErrorDto apiErrorDto = new ApiErrorDto(
            request.getRequestURI(),
            exception.getMessage(),
            HttpStatus.NOT_FOUND.value(),
            LocalDateTime.now()
        );

        return new ResponseEntity<ApiErrorDto>(apiErrorDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UsuarioExistenteException.class)
    public ResponseEntity<ApiErrorDto> handleUsuarioExistenteException(UsuarioExistenteException exception, HttpServletRequest request) {
        ApiErrorDto apiErrorDto = new ApiErrorDto(
            request.getRequestURI(),
            exception.getMessage(),
            HttpStatus.CONFLICT.value(),
            LocalDateTime.now()
        );

        return new ResponseEntity<ApiErrorDto>(apiErrorDto, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiErrorDto> handleIllegalStateException(IllegalStateException exception, HttpServletRequest request) {
        ApiErrorDto apiErrorDto = new ApiErrorDto(
            request.getRequestURI(),
            exception.getMessage(),
            HttpStatus.CONFLICT.value(),
            LocalDateTime.now()
        );

        return new ResponseEntity<ApiErrorDto>(apiErrorDto, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MailException.class)
    public ResponseEntity<ApiErrorDto> handleMailException(MailException exception, HttpServletRequest request) {
        String mensagem = "Ocorreu um erro ao enviar o email. Por favor, tente novamente";

        ApiErrorDto apiErrorDto = new ApiErrorDto(
            request.getRequestURI(),
            mensagem,
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            LocalDateTime.now()
        );

        return new ResponseEntity<ApiErrorDto>(apiErrorDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
