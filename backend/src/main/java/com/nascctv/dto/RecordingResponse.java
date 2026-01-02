package com.nascctv.dto;

import java.time.Instant;

public record RecordingResponse(
    Long id,
    Long cameraId,
    String storagePath,
    String format,
    Long sizeBytes,
    Instant startedAt,
    Instant endedAt,
    Integer retentionDays
) {}
