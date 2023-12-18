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
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import br.com.transportae.usuario.exceptions.UsuarioExistenteException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class DefaultExceptionHandler {

    @ExceptionHandler({Exception.class, RuntimeException.class})
    public ResponseEntity<ApiErrorDto> handleException(Exception exception, HttpServletRequest request) {
        exception.printStackTrace();
        
        ApiErrorDto apiErrorDto = new ApiErrorDto(
            request.getRequestURI(),
            "ERRO_DESCONHECIDO",
            exception.getMessage(),
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            LocalDateTime.now()
        );

        return new ResponseEntity<ApiErrorDto>(apiErrorDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception, HttpServletRequest request) {
        List<CampoInvalidoDto> errosDeCampos = exception.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(fieldError -> new CampoInvalidoDto(
                    fieldError.getField(), 
                    fieldError.getDefaultMessage()
                )
            )
            .toList();

        List<String> errosDeObjeto = exception.getBindingResult()
            .getGlobalErrors()
            .stream()
            .map(ObjectError::getDefaultMessage)
            .toList();
        
        String mensagem = errosDeObjeto.size() > 0
            ? errosDeObjeto.get(0)
            : "Um ou mais campos são inválidos";
        
        ApiErrorDto apiErrorDto = new ApiErrorDto(
            request.getRequestURI(),
            "CAMPOS_INVALIDOS",
            mensagem,
            HttpStatus.BAD_REQUEST.value(),
            LocalDateTime.now(),
            errosDeCampos
        );

        return new ResponseEntity<ApiErrorDto>(apiErrorDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiErrorDto> handleUsernameNotFoundException(UsernameNotFoundException exception, HttpServletRequest request) {
        ApiErrorDto apiErrorDto = new ApiErrorDto(
            request.getRequestURI(),
            "USUARIO_INEXISTENTE",
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
            "TOKEN_INVALIDO",
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
            "TOKEN_EXPIRADO",
            mensagem,
            HttpStatus.FORBIDDEN.value(),
            LocalDateTime.now()
        );

        return new ResponseEntity<ApiErrorDto>(apiErrorDto, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiErrorDto> handleBadCredentialsException(BadCredentialsException exception, HttpServletRequest request) {
        String mensagem = "Usuário e/ou senha incorretos";

        ApiErrorDto apiErrorDto = new ApiErrorDto(
            request.getRequestURI(),
            "CREDENCIAIS_INCORRETAS",
            mensagem,
            HttpStatus.BAD_REQUEST.value(),
            LocalDateTime.now()
        );

        return new ResponseEntity<ApiErrorDto>(apiErrorDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiErrorDto> handleAccessDeniedException(AccessDeniedException exception, HttpServletRequest request) {
        ApiErrorDto apiErrorDto = new ApiErrorDto(
            request.getRequestURI(),
            "ACESSO_NEGADO",
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
            "ENTIDADE_INEXISTENTE",
            exception.getMessage(),
            HttpStatus.NOT_FOUND.value(),
            LocalDateTime.now()
        );

        return new ResponseEntity<ApiErrorDto>(apiErrorDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<ApiErrorDto> handleEntityExistsException(EntityExistsException exception, HttpServletRequest request) {
        ApiErrorDto apiErrorDto = new ApiErrorDto(
            request.getRequestURI(),
            "ENTIDADE_DUPLICADA",
            exception.getMessage(),
            HttpStatus.CONFLICT.value(),
            LocalDateTime.now()
        );

        return new ResponseEntity<ApiErrorDto>(apiErrorDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UsuarioExistenteException.class)
    public ResponseEntity<ApiErrorDto> handleUsuarioExistenteException(UsuarioExistenteException exception, HttpServletRequest request) {
        ApiErrorDto apiErrorDto = new ApiErrorDto(
            request.getRequestURI(),
            "USUARIO_DUPLICADO",
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
            "DADOS_CONFLITANTES",
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
            "ERRO_EMAIL",            
            mensagem,
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            LocalDateTime.now()
        );

        return new ResponseEntity<ApiErrorDto>(apiErrorDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
