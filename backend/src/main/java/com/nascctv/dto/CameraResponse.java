package com.nascctv.dto;

public record CameraResponse(
    Long id,
    String name,
    String protocol,
    String streamUrl,
    String status,
    String location
) {}
