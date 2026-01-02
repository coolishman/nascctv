package com.nascctv.model;

import java.time.Instant;

public class Camera {
    private Long id;
    private String name;
    private String protocol;
    private String vendor;
    private String model;
    private String authType;
    private String streamUrl;
    private String status;
    private String location;
    private Instant createdAt;

    public Camera() {
    }

    public Camera(
        Long id,
        String name,
        String protocol,
        String vendor,
        String model,
        String authType,
        String streamUrl,
        String status,
        String location,
        Instant createdAt
    ) {
        this.id = id;
        this.name = name;
        this.protocol = protocol;
        this.vendor = vendor;
        this.model = model;
        this.authType = authType;
        this.streamUrl = streamUrl;
        this.status = status;
        this.location = location;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getAuthType() {
        return authType;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }

    public String getStreamUrl() {
        return streamUrl;
    }

    public void setStreamUrl(String streamUrl) {
        this.streamUrl = streamUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
