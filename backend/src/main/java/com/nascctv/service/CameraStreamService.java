package com.nascctv.service;

import com.nascctv.dto.CameraStreamRequest;
import com.nascctv.mapper.CameraMapper;
import com.nascctv.mapper.CameraStreamMapper;
import com.nascctv.model.CameraStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CameraStreamService {
    private final CameraStreamMapper cameraStreamMapper;
    private final CameraMapper cameraMapper;
    private final String webrtcBaseUrl;
    private final String webrtcPreviewPath;

    public CameraStreamService(
        CameraStreamMapper cameraStreamMapper,
        CameraMapper cameraMapper,
        @Value("${media.webrtc-base-url:}") String webrtcBaseUrl,
        @Value("${media.webrtc-preview-path:/preview/{cameraId}}") String webrtcPreviewPath
    ) {
        this.cameraStreamMapper = cameraStreamMapper;
        this.cameraMapper = cameraMapper;
        this.webrtcBaseUrl = webrtcBaseUrl;
        this.webrtcPreviewPath = webrtcPreviewPath;
    }

    public List<CameraStream> listByCamera(Long cameraId) {
        List<CameraStream> streams = new ArrayList<>(cameraStreamMapper.findByCameraId(cameraId));
        if (streams.stream().noneMatch((stream) -> "WEBRTC".equalsIgnoreCase(stream.getStreamType()))) {
            buildWebRtcStream(cameraId).ifPresent(streams::add);
        }
        return streams;
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

    private Optional<CameraStream> buildWebRtcStream(Long cameraId) {
        if (webrtcBaseUrl == null || webrtcBaseUrl.isBlank()) {
            return Optional.empty();
        }
        return cameraMapper.findById(cameraId).map((camera) -> {
            String base = webrtcBaseUrl.endsWith("/")
                ? webrtcBaseUrl.substring(0, webrtcBaseUrl.length() - 1)
                : webrtcBaseUrl;
            String path = webrtcPreviewPath.replace("{cameraId}", cameraId.toString());
            String streamUrl = base + (path.startsWith("/") ? path : "/" + path);
            return new CameraStream(
                null,
                cameraId,
                "WebRTC 预览",
                "WEBRTC",
                streamUrl,
                "H264",
                null,
                null
            );
        });
    }
}
