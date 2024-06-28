package com.ccunarro.user_project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    @Query("""
           SELECT u FROM User u
           LEFT JOIN FETCH u.externalProjects
           WHERE u.id = :userId
           """)
    User findUserWithExternalProjects(UUID userId);

    @Query("""
           SELECT CASE WHEN COUNT(uep) > 0 then true ELSE FALSE END
           FROM UserExternalProject uep
           WHERE uep.externalProjectId = :externalProjectId
           """)
    boolean existsExternalProject(String externalProjectId);

    @Query("""
           SELECT CASE WHEN COUNT(u) > 0 then true ELSE FALSE END
           FROM User u
           WHERE u.email = :email
           """)
    boolean existsUserWithEmail(String email);

    @Query("""
           SELECT COUNT(uep)
           FROM UserExternalProject uep
           WHERE uep.user.id = :userId
           """)
    int countExternalProjects(UUID userId);

    @Query("""
           SELECT u FROM User u
           WHERE u.email = :email
           """)
    User findByEmail(String email);
}
