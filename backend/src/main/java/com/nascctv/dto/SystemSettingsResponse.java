package com.nascctv.dto;

import java.time.Instant;

public record SystemSettingsResponse(
    Long id,
    Integer retentionDays,
    Boolean alertSound,
    Boolean autoRotate,
    Instant updatedAt
) {}
