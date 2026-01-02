package com.nascctv.model;

public class WallTileChannel {
    private Long id;
    private Long tileId;
    private Long cameraId;
    private Integer orderIndex;

    public WallTileChannel() {
    }

    public WallTileChannel(Long id, Long tileId, Long cameraId, Integer orderIndex) {
        this.id = id;
        this.tileId = tileId;
        this.cameraId = cameraId;
        this.orderIndex = orderIndex;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTileId() {
        return tileId;
    }

    public void setTileId(Long tileId) {
        this.tileId = tileId;
    }

    public Long getCameraId() {
        return cameraId;
    }

    public void setCameraId(Long cameraId) {
        this.cameraId = cameraId;
    }

    public Integer getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }
}
