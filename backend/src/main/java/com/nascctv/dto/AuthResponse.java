package com.nascctv.dto;

public record AuthResponse(
    String accessToken,
    String tokenType,
    String lastLoginIp
) {}
