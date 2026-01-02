package com.nascctv.dto;

import java.util.List;

public record VideoWallResponse(
    Long id,
    String name,
    String description,
    List<VideoWallTileResponse> tiles
) {}
