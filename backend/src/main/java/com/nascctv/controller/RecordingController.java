package com.nascctv.controller;

import com.nascctv.dto.RecordingRequest;
import com.nascctv.dto.RecordingResponse;
import com.nascctv.model.Recording;
import com.nascctv.security.UserPrincipal;
import com.nascctv.service.RecordingService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/recordings")
public class RecordingController {
    private final RecordingService recordingService;

    public RecordingController(RecordingService recordingService) {
        this.recordingService = recordingService;
    }

    @GetMapping
    public List<RecordingResponse> listRecordings(
        @RequestParam("cameraId") Long cameraId,
        @AuthenticationPrincipal UserPrincipal principal
    ) {
        return recordingService.listByCamera(cameraId, principal).stream()
            .map(RecordingController::toResponse)
            .toList();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public RecordingResponse createRecording(@Valid @RequestBody RecordingRequest request) {
        return toResponse(recordingService.createRecording(request));
    }

    private static RecordingResponse toResponse(Recording recording) {
        return new RecordingResponse(
            recording.getId(),
            recording.getCameraId(),
            recording.getStoragePath(),
            recording.getFormat(),
            recording.getSizeBytes(),
            recording.getStartedAt(),
            recording.getEndedAt(),
            recording.getRetentionDays()
        );
    }
}
