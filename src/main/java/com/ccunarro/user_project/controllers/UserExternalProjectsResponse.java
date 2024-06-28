package com.ccunarro.user_project.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserExternalProjectsResponse {

    private UUID id;
    private String name;
    private String email;
    private List<String> externalProjectIds = new ArrayList<>();


    public UserExternalProjectsResponse(UUID id, String name, String email, List<String> externalProjectIds) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.externalProjectIds = externalProjectIds;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getExternalProjectIds() {
        return externalProjectIds;
    }

    public void setExternalProjectIds(List<String> externalProjectIds) {
        this.externalProjectIds = externalProjectIds;
    }
}
