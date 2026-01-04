package com.nascctv.model;

import java.time.Instant;

public class WallTile {
    private Long id;
    private Long wallId;
    private Long cameraId;
    private Integer position;
    private Integer rowSpan;
    private Integer colSpan;
    private Integer rotationSeconds;
    private Boolean enabled;
    private Instant createdAt;

    public WallTile() {
    }

    public WallTile(
        Long id,
        Long wallId,
        Long cameraId,
        Integer position,
        Integer rowSpan,
        Integer colSpan,
        Integer rotationSeconds,
        Boolean enabled,
        Instant createdAt
    ) {
        this.id = id;
        this.wallId = wallId;
        this.cameraId = cameraId;
        this.position = position;
        this.rowSpan = rowSpan;
        this.colSpan = colSpan;
        this.rotationSeconds = rotationSeconds;
        this.enabled = enabled;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getWallId() {
        return wallId;
    }

    public void setWallId(Long wallId) {
        this.wallId = wallId;
    }

    public Long getCameraId() {
        return cameraId;
    }

    public void setCameraId(Long cameraId) {
        this.cameraId = cameraId;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Integer getRowSpan() {
        return rowSpan;
    }

    public void setRowSpan(Integer rowSpan) {
        this.rowSpan = rowSpan;
    }

    public Integer getColSpan() {
        return colSpan;
    }

    public void setColSpan(Integer colSpan) {
        this.colSpan = colSpan;
    }

    public Integer getRotationSeconds() {
        return rotationSeconds;
    }

    public void setRotationSeconds(Integer rotationSeconds) {
        this.rotationSeconds = rotationSeconds;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
