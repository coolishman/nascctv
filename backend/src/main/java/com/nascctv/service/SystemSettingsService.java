package com.nascctv.service;

import com.nascctv.dto.SystemSettingsRequest;
import com.nascctv.mapper.SystemSettingsMapper;
import com.nascctv.model.SystemSettings;
import org.springframework.stereotype.Service;

@Service
public class SystemSettingsService {
    private static final long DEFAULT_ID = 1L;

    private final SystemSettingsMapper systemSettingsMapper;

    public SystemSettingsService(SystemSettingsMapper systemSettingsMapper) {
        this.systemSettingsMapper = systemSettingsMapper;
    }

    public SystemSettings getSettings() {
        return systemSettingsMapper.findById(DEFAULT_ID)
            .orElseGet(() -> {
                SystemSettings settings = new SystemSettings(null, 30, true, true, null);
                systemSettingsMapper.insert(settings);
                return settings;
            });
    }

    public SystemSettings updateSettings(SystemSettingsRequest request) {
        SystemSettings settings = systemSettingsMapper.findById(DEFAULT_ID)
            .orElseGet(() -> {
                SystemSettings created = new SystemSettings(null, 30, true, true, null);
                systemSettingsMapper.insert(created);
                return created;
            });
        settings.setRetentionDays(request.retentionDays());
        settings.setAlertSound(request.alertSound());
        settings.setAutoRotate(request.autoRotate());
        systemSettingsMapper.update(settings);
        return settings;
    }
}
