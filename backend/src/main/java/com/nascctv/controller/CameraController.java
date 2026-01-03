package com.nascctv.controller;

import com.nascctv.dto.CameraRequest;
import com.nascctv.dto.CameraResponse;
import com.nascctv.dto.ProtocolProbeResponse;
import com.nascctv.model.Camera;
import com.nascctv.security.UserPrincipal;
import com.nascctv.service.CameraService;
import com.nascctv.service.ProtocolProbeService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/cameras")
public class CameraController {
    private final CameraService cameraService;
    private final ProtocolProbeService protocolProbeService;

    public CameraController(CameraService cameraService, ProtocolProbeService protocolProbeService) {
        this.cameraService = cameraService;
        this.protocolProbeService = protocolProbeService;
    }

    @GetMapping
    public List<CameraResponse> listCameras(@AuthenticationPrincipal UserPrincipal principal) {
        return cameraService.listCameras(principal).stream().map(CameraController::toResponse).toList();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public CameraResponse createCamera(@Valid @RequestBody CameraRequest request) {
        return toResponse(cameraService.createCamera(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public CameraResponse updateCamera(@PathVariable("id") Long id, @Valid @RequestBody CameraRequest request) {
        return toResponse(cameraService.updateCamera(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteCamera(@PathVariable("id") Long id) {
        cameraService.deleteCamera(id);
    }

    @PostMapping("/{id}/probe")
    @PreAuthorize("hasRole('ADMIN')")
    public ProtocolProbeResponse probeCamera(@PathVariable("id") Long id) {
        Camera camera = cameraService.getCamera(id);
        ProtocolProbeService.ProbeResult result = protocolProbeService.probe(camera);
        return new ProtocolProbeResponse(result.success(), result.message(), result.elapsedMs());
    }

    private static CameraResponse toResponse(Camera camera) {
        return new CameraResponse(
            camera.getId(),
            camera.getName(),
            camera.getProtocol(),
            camera.getVendor(),
            camera.getModel(),
            camera.getAuthType(),
            camera.getStreamUrl(),
            camera.getStatus(),
            camera.getLocation()
        );
    }
}
