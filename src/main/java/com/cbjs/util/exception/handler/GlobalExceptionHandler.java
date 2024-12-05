package com.cbjs.util.exception.handler;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.cbjs.util.exception.InputValidationException;
import com.cbjs.util.exception.model.ApiError;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError){
        return new ResponseEntity<>(apiError, apiError.getTitle());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(x -> x.getDefaultMessage())
                .collect(Collectors.toList());

        ApiError apiError = new ApiError(status.value(),
                status,
                LocalDateTime.now(),
                "Validation failed",
                "",
                errors);
        return new ResponseEntity<>(apiError, headers, status);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        ApiError apiError = new ApiError();
        apiError.setTitle(status);
        apiError.setStatus(status.value());
        apiError.setMessage("Media type not supported");
        return new ResponseEntity<>(apiError, headers, status);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex){
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND, ex.getMessage());
        return buildResponseEntity(apiError);
    }

    @Override
    protected ResponseEntity<Object> handleNoResourceFoundException(NoResourceFoundException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ApiError apiError = new ApiError(
                status.value(),
                status,
                "Resource not found: /" + ex.getResourcePath()
        );
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(InputValidationException.class)
    protected ResponseEntity<Object> handleInputValidation(InputValidationException ex){
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND.value(), HttpStatus.BAD_REQUEST, ex.getMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex){
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST, "Validation failed");
        apiError.setErrors(ex.getConstraintViolations().stream().map(ConstraintViolation::getMessageTemplate).collect(Collectors.toList()));
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleSecurityException(Exception ex){
        ApiError apiError = new ApiError(HttpStatus.NO_CONTENT.value(), HttpStatus.FORBIDDEN);
        if(ex instanceof BadCredentialsException){
            apiError.setMessage("Bad credentials");
            apiError.setTitle(HttpStatus.UNAUTHORIZED);
            apiError.setStatus(HttpStatus.UNAUTHORIZED.value());
            return buildResponseEntity(apiError);
        }
        if(ex instanceof AccessDeniedException){
            apiError.setMessage("Forbidden");
            apiError.setTitle(HttpStatus.FORBIDDEN);
            apiError.setStatus(HttpStatus.FORBIDDEN.value());
            return buildResponseEntity(apiError);
        }
        if(ex instanceof UsernameNotFoundException){
            apiError.setMessage("User not found");
            apiError.setTitle(HttpStatus.BAD_REQUEST);
            apiError.setStatus(HttpStatus.BAD_REQUEST.value());
            return buildResponseEntity(apiError);
        }
        if(ex instanceof SignatureException){
            apiError.setMessage("Invalid token");
            apiError.setTitle(HttpStatus.FORBIDDEN);
            apiError.setStatus(HttpStatus.FORBIDDEN.value());
            return buildResponseEntity(apiError);
        }
        if(ex instanceof ExpiredJwtException){
            apiError.setMessage("Token expired");
            apiError.setTitle(HttpStatus.FORBIDDEN);
            apiError.setStatus(HttpStatus.FORBIDDEN.value());
            return buildResponseEntity(apiError);
        }
        if(ex instanceof MalformedJwtException){
            apiError.setMessage("Invalid token");
            apiError.setTitle(HttpStatus.FORBIDDEN);
            apiError.setStatus(HttpStatus.FORBIDDEN.value());
            return buildResponseEntity(apiError);
        }
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex){
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST, ex.getMessage());
        return buildResponseEntity(apiError);
    }
}
