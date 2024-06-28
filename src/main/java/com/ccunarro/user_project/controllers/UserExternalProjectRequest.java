package com.ccunarro.user_project.controllers;

import jakarta.validation.constraints.NotEmpty;

public class UserExternalProjectRequest {

    @NotEmpty
    private String externalProjectId;

    public String getExternalProjectId() {
        return externalProjectId;
    }

    public void setExternalProjectId(String externalProjectId) {
        this.externalProjectId = externalProjectId;
    }
}
