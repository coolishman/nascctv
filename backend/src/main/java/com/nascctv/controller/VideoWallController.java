package com.nascctv.controller;

import com.nascctv.dto.VideoWallResponse;
import com.nascctv.dto.VideoWallUpdateRequest;
import com.nascctv.security.UserPrincipal;
import com.nascctv.service.VideoWallService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/api/walls", "/walls"})
public class VideoWallController {
    private final VideoWallService videoWallService;

    public VideoWallController(VideoWallService videoWallService) {
        this.videoWallService = videoWallService;
    }

    @GetMapping("/{wallId}")
    public VideoWallResponse getWall(
        @PathVariable("wallId") Long wallId,
        @AuthenticationPrincipal UserPrincipal principal
    ) {
        return videoWallService.getWall(wallId, principal);
    }

    @PutMapping("/{wallId}")
    @PreAuthorize("hasRole('ADMIN')")
    public VideoWallResponse updateWall(
        @PathVariable("wallId") Long wallId,
        @Valid @RequestBody VideoWallUpdateRequest request,
        @AuthenticationPrincipal UserPrincipal principal
    ) {
        return videoWallService.updateWall(wallId, request, principal);
    }
}
