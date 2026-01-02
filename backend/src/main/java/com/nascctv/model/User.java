package com.nascctv.model;

import java.time.Instant;

public record User(
    Long id,
    String username,
    String passwordHash,
    String role,
    Instant createdAt
) {}
