package com.nascctv.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record RecordingRequest(
    @NotNull Long cameraId,
    @NotBlank String storagePath,
    @NotBlank String format,
    @NotNull Long sizeBytes,
    @NotNull Instant startedAt,
    @NotNull Instant endedAt,
    Integer retentionDays
) {}
