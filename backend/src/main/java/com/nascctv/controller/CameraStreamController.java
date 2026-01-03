package com.nascctv.controller;

import com.nascctv.dto.CameraStreamRequest;
import com.nascctv.dto.CameraStreamResponse;
import com.nascctv.model.CameraStream;
import com.nascctv.service.CameraStreamService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/cameras/{cameraId}/streams")
public class CameraStreamController {
    private final CameraStreamService cameraStreamService;

    public CameraStreamController(CameraStreamService cameraStreamService) {
        this.cameraStreamService = cameraStreamService;
    }

    @GetMapping
    public List<CameraStreamResponse> listStreams(@PathVariable("cameraId") Long cameraId) {
        return cameraStreamService.listByCamera(cameraId).stream()
            .map(CameraStreamController::toResponse)
            .toList();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public CameraStreamResponse createStream(
        @PathVariable("cameraId") Long cameraId,
        @Valid @RequestBody CameraStreamRequest request
    ) {
        CameraStreamRequest normalizedRequest = new CameraStreamRequest(
            cameraId,
            request.name(),
            request.streamType(),
            request.streamUrl(),
            request.codec(),
            request.bitrateKbps()
        );
        return toResponse(cameraStreamService.createStream(normalizedRequest));
    }

    private static CameraStreamResponse toResponse(CameraStream stream) {
        return new CameraStreamResponse(
            stream.getId(),
            stream.getCameraId(),
            stream.getName(),
            stream.getStreamType(),
            stream.getStreamUrl(),
            stream.getCodec(),
            stream.getBitrateKbps()
        );
    }
}
