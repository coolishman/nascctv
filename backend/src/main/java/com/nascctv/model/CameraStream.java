package com.nascctv.model;

import java.time.Instant;

public class CameraStream {
    private Long id;
    private Long cameraId;
    private String name;
    private String streamType;
    private String streamUrl;
    private String codec;
    private Integer bitrateKbps;
    private Instant createdAt;

    public CameraStream() {
    }

    public CameraStream(
        Long id,
        Long cameraId,
        String name,
        String streamType,
        String streamUrl,
        String codec,
        Integer bitrateKbps,
        Instant createdAt
    ) {
        this.id = id;
        this.cameraId = cameraId;
        this.name = name;
        this.streamType = streamType;
        this.streamUrl = streamUrl;
        this.codec = codec;
        this.bitrateKbps = bitrateKbps;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStreamType() {
        return streamType;
    }

    public void setStreamType(String streamType) {
        this.streamType = streamType;
    }

    public String getStreamUrl() {
        return streamUrl;
    }

    public void setStreamUrl(String streamUrl) {
        this.streamUrl = streamUrl;
    }

    public String getCodec() {
        return codec;
    }

    public void setCodec(String codec) {
        this.codec = codec;
    }

    public Integer getBitrateKbps() {
        return bitrateKbps;
    }

    public void setBitrateKbps(Integer bitrateKbps) {
        this.bitrateKbps = bitrateKbps;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
