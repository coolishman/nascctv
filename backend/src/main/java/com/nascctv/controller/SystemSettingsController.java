package com.nascctv.controller;

import com.nascctv.dto.SystemSettingsRequest;
import com.nascctv.dto.SystemSettingsResponse;
import com.nascctv.model.SystemSettings;
import com.nascctv.service.SystemSettingsService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/settings")
public class SystemSettingsController {
    private final SystemSettingsService systemSettingsService;

    public SystemSettingsController(SystemSettingsService systemSettingsService) {
        this.systemSettingsService = systemSettingsService;
    }

    @GetMapping
    public SystemSettingsResponse getSettings() {
        return toResponse(systemSettingsService.getSettings());
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public SystemSettingsResponse updateSettings(@Valid @RequestBody SystemSettingsRequest request) {
        return toResponse(systemSettingsService.updateSettings(request));
    }

    private static SystemSettingsResponse toResponse(SystemSettings settings) {
        return new SystemSettingsResponse(
            settings.getId(),
            settings.getRetentionDays(),
            settings.getAlertSound(),
            settings.getAutoRotate(),
            settings.getUpdatedAt()
        );
    }
}
