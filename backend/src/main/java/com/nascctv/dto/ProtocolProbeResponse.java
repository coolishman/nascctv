package com.nascctv.dto;

public record ProtocolProbeResponse(
    boolean success,
    String message,
    Long elapsedMs
) {}
