package com.ccunarro.user_project.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_user")
public class User {

    @Id
    @GeneratedValue(generator = "system-uuid")
    private UUID id;

    @Column
    private String password;

    @Column
    private String email;

    @Column
    private String name;


    @OneToMany(mappedBy = "user", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    private List<UserExternalProject> externalProjects;

    @Column(insertable = false)
    private LocalDateTime created;

    public UUID getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<UserExternalProject> getExternalProjects() {
        return externalProjects;
    }

    public void setExternalProjects(List<UserExternalProject> externalProjects) {
        this.externalProjects = externalProjects;
    }

    public LocalDateTime getCreated() {
        return created;
    }
}
