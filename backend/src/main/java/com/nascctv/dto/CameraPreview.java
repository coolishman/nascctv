package com.nascctv.dto;

public record CameraPreview(
    Long id,
    String name,
    String protocol,
    String streamUrl
) {}
