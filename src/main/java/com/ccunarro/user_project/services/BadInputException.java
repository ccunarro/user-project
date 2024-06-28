package com.ccunarro.user_project.services;

public class BadInputException extends RuntimeException {
    public BadInputException(String message) {
        super(message);
    }
}
