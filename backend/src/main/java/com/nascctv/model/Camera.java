package com.nascctv.model;

import java.time.Instant;

public record Camera(
    Long id,
    String name,
    String protocol,
    String streamUrl,
    String status,
    String location,
    Instant createdAt
) {}
