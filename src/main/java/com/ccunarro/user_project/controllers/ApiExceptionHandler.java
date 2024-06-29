package com.ccunarro.user_project.controllers;

import com.ccunarro.user_project.services.BadInputException;
import com.ccunarro.user_project.services.InvalidExternalProjectException;
import com.ccunarro.user_project.services.ResourceNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.TreeSet;
import java.util.UUID;

@ControllerAdvice
public class ApiExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ApiExceptionHandler.class);

    @ExceptionHandler(InvalidExternalProjectException.class)
    public ResponseEntity<ApiResponse> invalidExternalProjectException(InvalidExternalProjectException e) {
        var apiResponse = ApiResponse.apiResponseError(List.of(e.getMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
    }

    @ExceptionHandler(BadInputException.class)
    public ResponseEntity<ApiResponse> badInputException(BadInputException e) {
        var apiResponse = ApiResponse.apiResponseError(List.of(e.getMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> resourceNotFoundException(ResourceNotFoundException e) {
        var apiResponse = ApiResponse.apiResponseError(List.of(e.getMessage()));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
    }

    /**
     * This is to handle the exceptions at the controller level with the jakarta.validation.constraints used for example in UserRequest class
     */
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ApiResponse> invalidParameterException(MethodArgumentNotValidException
                                                                             exception) {
       var fieldErrors = exception.getFieldErrors();
       TreeSet<String> errors = new TreeSet<>();
       for (FieldError e : fieldErrors) {
           errors.add(String.format("Invalid parameter '%s' %s", e.getField(), e.getDefaultMessage()));
       }
       return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.apiResponseError(errors));
    }

    @ExceptionHandler({AuthorizationDeniedException.class})
    public ResponseEntity<ApiResponse> authorizationDeniedException(AuthorizationDeniedException exception) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.apiResponseError(List.of("Unauthorized to perform this action")));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> unhandledExceptions(Exception e) {
        var uuid = UUID.randomUUID().toString();
        LOG.warn("Unhandled exception, uuid: {}, exception: {}", uuid, e.getMessage(), e);

        var apiResponse = ApiResponse.apiResponseError(List.of("Unexpected error, uuid: " + uuid));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
    }
}
