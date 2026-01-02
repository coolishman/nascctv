package com.nascctv.service;

import com.nascctv.dto.RecordingRequest;
import com.nascctv.mapper.RecordingMapper;
import com.nascctv.model.Recording;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecordingService {
    private final RecordingMapper recordingMapper;

    public RecordingService(RecordingMapper recordingMapper) {
        this.recordingMapper = recordingMapper;
    }

    public List<Recording> listByCamera(Long cameraId) {
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
}
