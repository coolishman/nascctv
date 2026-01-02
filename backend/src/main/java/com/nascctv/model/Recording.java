package com.nascctv.model;

import java.time.Instant;

public class Recording {
    private Long id;
    private Long cameraId;
    private String storagePath;
    private String format;
    private Long sizeBytes;
    private Instant startedAt;
    private Instant endedAt;
    private Integer retentionDays;
    private Instant createdAt;

    public Recording() {
    }

    public Recording(
        Long id,
        Long cameraId,
        String storagePath,
        String format,
        Long sizeBytes,
        Instant startedAt,
        Instant endedAt,
        Integer retentionDays,
        Instant createdAt
    ) {
        this.id = id;
        this.cameraId = cameraId;
        this.storagePath = storagePath;
        this.format = format;
        this.sizeBytes = sizeBytes;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
        this.retentionDays = retentionDays;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCameraId() {
        return cameraId;
    }

    public void setCameraId(Long cameraId) {
        this.cameraId = cameraId;
    }

    public String getStoragePath() {
        return storagePath;
    }

    public void setStoragePath(String storagePath) {
        this.storagePath = storagePath;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public Long getSizeBytes() {
        return sizeBytes;
    }

    public void setSizeBytes(Long sizeBytes) {
        this.sizeBytes = sizeBytes;
    }

    public Instant getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Instant startedAt) {
        this.startedAt = startedAt;
    }

    public Instant getEndedAt() {
        return endedAt;
    }

    public void setEndedAt(Instant endedAt) {
        this.endedAt = endedAt;
    }

    public Integer getRetentionDays() {
        return retentionDays;
    }

    public void setRetentionDays(Integer retentionDays) {
        this.retentionDays = retentionDays;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
