package com.nascctv.service;

import com.nascctv.model.Camera;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@Service
public class ProtocolProbeService {
    public ProbeResult probe(Camera camera) {
        if (camera == null) {
            return new ProbeResult(false, "Camera not found", 0L);
        }
        String protocol = camera.getProtocol() != null ? camera.getProtocol().toUpperCase() : "";
        return switch (protocol) {
            case "RTSP" -> probeRtsp(camera.getStreamUrl());
            case "ONVIF" -> probeOnvif(camera.getStreamUrl());
            case "GB/T 28181" -> probeGb28181(camera.getStreamUrl());
            default -> new ProbeResult(false, "Unsupported protocol: " + camera.getProtocol(), 0L);
        };
    }

    private ProbeResult probeRtsp(String streamUrl) {
        long start = System.currentTimeMillis();
        try {
            URI uri = URI.create(streamUrl);
            String host = uri.getHost();
            if (host == null || host.isBlank()) {
                return new ProbeResult(false, "RTSP url missing host", elapsed(start));
            }
            int port = uri.getPort() > 0 ? uri.getPort() : 554;
            try (Socket socket = new Socket()) {
                socket.connect(new InetSocketAddress(host, port), 3000);
                socket.setSoTimeout(3000);
                String request = "OPTIONS " + streamUrl + " RTSP/1.0\r\nCSeq: 1\r\n\r\n";
                socket.getOutputStream().write(request.getBytes());
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                    String line = reader.readLine();
                    if (line != null && line.startsWith("RTSP/1.0")) {
                        return new ProbeResult(true, line, elapsed(start));
                    }
                }
            }
            return new ProbeResult(false, "No RTSP response", elapsed(start));
        } catch (Exception ex) {
            return new ProbeResult(false, "RTSP probe failed: " + ex.getMessage(), elapsed(start));
        }
    }

    private ProbeResult probeOnvif(String url) {
        long start = System.currentTimeMillis();
        try {
            URI uri = URI.create(url);
            if (!"http".equalsIgnoreCase(uri.getScheme()) && !"https".equalsIgnoreCase(uri.getScheme())) {
                return new ProbeResult(false, "ONVIF url must be http/https", elapsed(start));
            }
            if (uri.getHost() == null || uri.getHost().isBlank()) {
                return new ProbeResult(false, "ONVIF url missing host", elapsed(start));
            }
            HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(3))
                .build();
            HttpRequest request = HttpRequest.newBuilder(uri)
                .timeout(Duration.ofSeconds(3))
                .GET()
                .build();
            HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
            return new ProbeResult(true, "HTTP " + response.statusCode(), elapsed(start));
        } catch (Exception ex) {
            return new ProbeResult(false, "ONVIF probe failed: " + ex.getMessage(), elapsed(start));
        }
    }

    private ProbeResult probeGb28181(String sipUri) {
        long start = System.currentTimeMillis();
        try {
            String[] parts = sipUri.split("@", 2);
            String hostPart = parts.length == 2 ? parts[1] : sipUri;
            String host = hostPart;
            int port = 5060;
            if (hostPart.contains(":")) {
                String[] hostParts = hostPart.split(":", 2);
                host = hostParts[0];
                port = Integer.parseInt(hostParts[1]);
            }
            if (host == null || host.isBlank()) {
                return new ProbeResult(false, "GB/T 28181 url missing host", elapsed(start));
            }
            try (Socket socket = new Socket()) {
                socket.connect(new InetSocketAddress(host, port), 3000);
            }
            return new ProbeResult(true, "TCP connection ok", elapsed(start));
        } catch (Exception ex) {
            return new ProbeResult(false, "GB/T 28181 probe failed: " + ex.getMessage(), elapsed(start));
        }
    }

    private long elapsed(long start) {
        return System.currentTimeMillis() - start;
    }

    public record ProbeResult(boolean success, String message, long elapsedMs) {}
}
