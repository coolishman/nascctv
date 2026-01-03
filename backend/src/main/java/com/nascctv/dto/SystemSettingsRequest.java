package com.nascctv.dto;

import jakarta.validation.constraints.NotNull;

public record SystemSettingsRequest(
    @NotNull Integer retentionDays,
    @NotNull Boolean alertSound,
    @NotNull Boolean autoRotate
) {}
