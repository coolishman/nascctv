package com.nascctv.model;

import java.time.Instant;

public class SystemSettings {
    private Long id;
    private Integer retentionDays;
    private Boolean alertSound;
    private Boolean autoRotate;
    private Instant updatedAt;

    public SystemSettings() {
    }

    public SystemSettings(Long id, Integer retentionDays, Boolean alertSound, Boolean autoRotate, Instant updatedAt) {
        this.id = id;
        this.retentionDays = retentionDays;
        this.alertSound = alertSound;
        this.autoRotate = autoRotate;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRetentionDays() {
        return retentionDays;
    }

    public void setRetentionDays(Integer retentionDays) {
        this.retentionDays = retentionDays;
    }

    public Boolean getAlertSound() {
        return alertSound;
    }

    public void setAlertSound(Boolean alertSound) {
        this.alertSound = alertSound;
    }

    public Boolean getAutoRotate() {
        return autoRotate;
    }

    public void setAutoRotate(Boolean autoRotate) {
        this.autoRotate = autoRotate;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
