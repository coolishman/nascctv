package com.nascctv.dto;

import jakarta.validation.constraints.NotNull;

public record WallTileUpdateRequest(
    @NotNull Integer position,
    @NotNull Integer rowSpan,
    @NotNull Integer colSpan,
    @NotNull Integer rotationSeconds,
    @NotNull Boolean enabled,
    Long cameraId
) {}
