package com.nascctv.service;

import com.nascctv.dto.CameraStreamRequest;
import com.nascctv.mapper.CameraStreamMapper;
import com.nascctv.model.CameraStream;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CameraStreamService {
    private final CameraStreamMapper cameraStreamMapper;

    public CameraStreamService(CameraStreamMapper cameraStreamMapper) {
        this.cameraStreamMapper = cameraStreamMapper;
    }

    public List<CameraStream> listByCamera(Long cameraId) {
        return cameraStreamMapper.findByCameraId(cameraId);
    }

    public CameraStream createStream(CameraStreamRequest request) {
        CameraStream stream = new CameraStream(
            null,
            request.cameraId(),
            request.name(),
            request.streamType(),
            request.streamUrl(),
            request.codec(),
            request.bitrateKbps(),
            null
        );
        cameraStreamMapper.insert(stream);
        return stream;
    }
}
