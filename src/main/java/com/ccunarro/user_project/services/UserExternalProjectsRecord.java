package com.ccunarro.user_project.services;

import java.util.List;
import java.util.UUID;

public record UserExternalProjectsRecord(UUID id, String email, String name, List<String> externalProjectIds) {
}
