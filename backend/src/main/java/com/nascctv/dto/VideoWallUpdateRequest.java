package com.nascctv.dto;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record VideoWallUpdateRequest(
    String name,
    String description,
    @NotEmpty List<WallTileUpdateRequest> tiles
) {}
