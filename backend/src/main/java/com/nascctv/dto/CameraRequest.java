package com.nascctv.dto;

import jakarta.validation.constraints.NotBlank;

public record CameraRequest(
    @NotBlank String name,
    @NotBlank String protocol,
    String vendor,
    String model,
    @NotBlank String authType,
    @NotBlank String streamUrl,
    @NotBlank String status,
    String location
) {}
