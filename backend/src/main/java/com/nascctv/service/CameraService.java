package com.nascctv.service;

import com.nascctv.dto.CameraRequest;
import com.nascctv.mapper.CameraMapper;
import com.nascctv.mapper.UserCameraPermissionMapper;
import com.nascctv.mapper.UserMapper;
import com.nascctv.model.Camera;
import com.nascctv.model.User;
import com.nascctv.security.UserPrincipal;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class CameraService {
    private final CameraMapper cameraMapper;
    private final UserMapper userMapper;
    private final UserCameraPermissionMapper userCameraPermissionMapper;

    public CameraService(
        CameraMapper cameraMapper,
        UserMapper userMapper,
        UserCameraPermissionMapper userCameraPermissionMapper
    ) {
        this.cameraMapper = cameraMapper;
        this.userMapper = userMapper;
        this.userCameraPermissionMapper = userCameraPermissionMapper;
    }

    public List<Camera> listCameras(UserPrincipal principal) {
        boolean isAdmin = principal.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .anyMatch(authority -> authority.equals("ROLE_ADMIN"));
        if (isAdmin) {
            return cameraMapper.findAll();
        }
        User user = userMapper.findByUsername(principal.getUsername())
            .orElseThrow(() -> new IllegalStateException("User not found"));
        Set<Long> allowedCameraIds = Set.copyOf(userCameraPermissionMapper.findCameraIdsByUserId(user.getId()));
        if (allowedCameraIds.isEmpty()) {
            return List.of();
        }
        return cameraMapper.findByIds(List.copyOf(allowedCameraIds));
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
