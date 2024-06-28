package com.ccunarro.user_project.repositories;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_user_external_project")
public class UserExternalProject {

    @Id
    @GeneratedValue(generator = "system-uuid")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Column
    private String externalProjectId;

    @Column(insertable = false)
    private LocalDateTime created;

    public UserExternalProject() {
    }

    public UserExternalProject(User user, String externalProjectId) {
        this.user = user;
        this.externalProjectId = externalProjectId;
    }

    public UUID getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getExternalProjectId() {
        return externalProjectId;
    }

    public void setExternalProjectId(String externalProjectId) {
        this.externalProjectId = externalProjectId;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }
}
