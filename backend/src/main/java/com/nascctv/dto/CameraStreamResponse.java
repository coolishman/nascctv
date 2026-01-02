package com.nascctv.dto;

public record CameraStreamResponse(
    Long id,
    Long cameraId,
    String name,
    String streamType,
    String streamUrl,
    String codec,
    Integer bitrateKbps
) {}
