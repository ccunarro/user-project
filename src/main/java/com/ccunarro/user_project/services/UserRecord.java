package com.ccunarro.user_project.services;

import java.util.UUID;

public record UserRecord(UUID id, String email, String name) {
}
