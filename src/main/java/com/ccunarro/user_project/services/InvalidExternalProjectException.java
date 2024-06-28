package com.ccunarro.user_project.services;

public class InvalidExternalProjectException extends RuntimeException {
    public InvalidExternalProjectException(String messsage) {
        super(messsage);
    }
}
