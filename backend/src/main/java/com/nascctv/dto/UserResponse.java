package com.nascctv.dto;

import java.time.Instant;

public record UserResponse(
    Long id,
    String username,
    String role,
    String lastLoginIp,
    Instant createdAt
) {}
