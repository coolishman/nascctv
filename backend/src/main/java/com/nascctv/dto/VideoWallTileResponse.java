package com.nascctv.dto;

import java.util.List;

public record VideoWallTileResponse(
    Long id,
    Integer position,
    Integer rowSpan,
    Integer colSpan,
    Integer rotationSeconds,
    Boolean enabled,
    List<CameraPreview> playlist
) {}
