package com.nascctv.service;

import com.nascctv.dto.CameraRequest;
import com.nascctv.mapper.CameraMapper;
import com.nascctv.model.Camera;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CameraService {
    private final CameraMapper cameraMapper;

    public CameraService(CameraMapper cameraMapper) {
        this.cameraMapper = cameraMapper;
    }

    public List<Camera> listCameras() {
        return cameraMapper.findAll();
    }

    public Camera createCamera(CameraRequest request) {
        Camera camera = new Camera(
            null,
            request.name(),
            request.protocol(),
            request.vendor(),
            request.model(),
            request.authType(),
            request.streamUrl(),
            request.status(),
            request.location(),
            null
        );
        cameraMapper.insert(camera);
        return camera;
    }

    public Camera updateCamera(Long id, CameraRequest request) {
        Camera camera = new Camera(
            id,
            request.name(),
            request.protocol(),
            request.vendor(),
            request.model(),
            request.authType(),
            request.streamUrl(),
            request.status(),
            request.location(),
            null
        );
        cameraMapper.update(camera);
        return camera;
    }

    public void deleteCamera(Long id) {
        cameraMapper.delete(id);
    }
}
