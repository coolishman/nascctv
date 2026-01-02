package com.nascctv.dto;

public record CameraResponse(
    Long id,
    String name,
    String protocol,
    String vendor,
    String model,
    String authType,
    String streamUrl,
    String status,
    String location
) {}
