package com.nascctv.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CameraStreamRequest(
    @NotNull Long cameraId,
    @NotBlank String name,
    @NotBlank String streamType,
    @NotBlank String streamUrl,
    String codec,
    Integer bitrateKbps
) {}
