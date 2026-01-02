package com.nascctv.dto;

import jakarta.validation.constraints.NotBlank;

public record CameraRequest(
    @NotBlank String name,
    @NotBlank String protocol,
    @NotBlank String streamUrl,
    @NotBlank String status,
    String location
) {}
