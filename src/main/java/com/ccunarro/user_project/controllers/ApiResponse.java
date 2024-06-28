package com.ccunarro.user_project.controllers;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Collection;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse {

    String message;
    Collection<String> errors;

    private ApiResponse(String message) {
        this.message = message;
    }

    private ApiResponse(Collection<String> errors) {
        this.errors = errors;
    }


    public static ApiResponse apiResponseOk(String message) {
        return new ApiResponse(message);
    }

    public static ApiResponse apiResponseError(Collection<String> errors) {
        return new ApiResponse(errors);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Collection<String> getErrors() {
        return errors;
    }

    public void setErrors(Collection<String> errors) {
        this.errors = errors;
    }
}
