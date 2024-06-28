package com.ccunarro.user_project.controllers;

import com.ccunarro.user_project.services.UserExternalProjectsRecord;
import com.ccunarro.user_project.services.UserRecord;

import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponse map(UserRecord rec) {
        return new UserResponse(rec.id(), rec.name(), rec.email());
    }

    public UserExternalProjectsResponse map(UserExternalProjectsRecord rec) {
        return new UserExternalProjectsResponse(rec.id(), rec.name(), rec.email(), rec.externalProjectIds());
    }
}
