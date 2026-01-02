package com.nascctv.service;

import com.nascctv.dto.RecordingRequest;
import com.nascctv.mapper.RecordingMapper;
import com.nascctv.mapper.UserCameraPermissionMapper;
import com.nascctv.mapper.UserMapper;
import com.nascctv.model.Recording;
import com.nascctv.model.User;
import com.nascctv.security.UserPrincipal;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class RecordingService {
    private final RecordingMapper recordingMapper;
    private final UserMapper userMapper;
    private final UserCameraPermissionMapper userCameraPermissionMapper;

    public RecordingService(
        RecordingMapper recordingMapper,
        UserMapper userMapper,
        UserCameraPermissionMapper userCameraPermissionMapper
    ) {
        this.recordingMapper = recordingMapper;
        this.userMapper = userMapper;
        this.userCameraPermissionMapper = userCameraPermissionMapper;
    }

    public List<Recording> listByCamera(Long cameraId, UserPrincipal principal) {
        if (!isAdmin(principal) && !hasCameraPermission(cameraId, principal)) {
            throw new AccessDeniedException("No permission to access recordings for this camera.");
        }
        return recordingMapper.findByCameraId(cameraId);
    }

    public Recording createRecording(RecordingRequest request) {
        Recording recording = new Recording(
            null,
            request.cameraId(),
            request.storagePath(),
            request.format(),
            request.sizeBytes(),
            request.startedAt(),
            request.endedAt(),
            request.retentionDays() != null ? request.retentionDays() : 30,
            null
        );
        recordingMapper.insert(recording);
        return recording;
    }

    private boolean isAdmin(UserPrincipal principal) {
        return principal.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .anyMatch(authority -> authority.equals("ROLE_ADMIN"));
    }

    private boolean hasCameraPermission(Long cameraId, UserPrincipal principal) {
        User user = userMapper.findByUsername(principal.getUsername())
            .orElseThrow(() -> new IllegalStateException("User not found"));
        Set<Long> allowedCameraIds = Set.copyOf(userCameraPermissionMapper.findCameraIdsByUserId(user.getId()));
        return allowedCameraIds.contains(cameraId);
    }
}
