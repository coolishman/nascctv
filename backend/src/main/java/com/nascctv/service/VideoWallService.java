package com.nascctv.service;

import com.nascctv.dto.CameraPreview;
import com.nascctv.dto.VideoWallResponse;
import com.nascctv.dto.VideoWallTileResponse;
import com.nascctv.mapper.CameraMapper;
import com.nascctv.mapper.UserCameraPermissionMapper;
import com.nascctv.mapper.UserMapper;
import com.nascctv.mapper.VideoWallMapper;
import com.nascctv.mapper.WallTileChannelMapper;
import com.nascctv.mapper.WallTileMapper;
import com.nascctv.model.Camera;
import com.nascctv.model.User;
import com.nascctv.model.VideoWall;
import com.nascctv.model.WallTile;
import com.nascctv.security.UserPrincipal;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class VideoWallService {
    private final VideoWallMapper videoWallMapper;
    private final WallTileMapper wallTileMapper;
    private final WallTileChannelMapper wallTileChannelMapper;
    private final UserMapper userMapper;
    private final UserCameraPermissionMapper userCameraPermissionMapper;
    private final CameraMapper cameraMapper;

    public VideoWallService(
        VideoWallMapper videoWallMapper,
        WallTileMapper wallTileMapper,
        WallTileChannelMapper wallTileChannelMapper,
        UserMapper userMapper,
        UserCameraPermissionMapper userCameraPermissionMapper,
        CameraMapper cameraMapper
    ) {
        this.videoWallMapper = videoWallMapper;
        this.wallTileMapper = wallTileMapper;
        this.wallTileChannelMapper = wallTileChannelMapper;
        this.userMapper = userMapper;
        this.userCameraPermissionMapper = userCameraPermissionMapper;
        this.cameraMapper = cameraMapper;
    }

    public VideoWallResponse getWall(Long wallId, UserPrincipal principal) {
        VideoWall wall = videoWallMapper.findById(wallId)
            .orElseThrow(() -> new IllegalArgumentException("Wall not found"));
        boolean isAdmin = principal.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .anyMatch(authority -> authority.equals("ROLE_ADMIN"));

        Set<Long> allowedCameraIds = null;
        if (!isAdmin) {
            User user = userMapper.findByUsername(principal.getUsername())
                .orElseThrow(() -> new IllegalStateException("User not found"));
            allowedCameraIds = Set.copyOf(userCameraPermissionMapper.findCameraIdsByUserId(user.getId()));
        }

        List<WallTile> tiles = wallTileMapper.findByWallId(wallId);
        Map<Long, List<CameraPreview>> playlists = wallTileChannelMapper.findChannelsByWallId(wallId).stream()
            .collect(Collectors.groupingBy(
                WallTileChannelMapper.WallTileChannelView::tileId,
                Collectors.mapping(WallTileChannelMapper.WallTileChannelView::toPreview, Collectors.toList())
            ));

        List<VideoWallTileResponse> responseTiles = new ArrayList<>();
        for (WallTile tile : tiles) {
            List<CameraPreview> playlist = new ArrayList<>(
                playlists.getOrDefault(tile.getId(), List.of())
            );

            if (playlist.isEmpty() && tile.getCameraId() != null) {
                Camera camera = cameraMapper.findById(tile.getCameraId()).orElse(null);
                if (camera != null) {
                    playlist.add(new CameraPreview(
                        camera.getId(),
                        camera.getName(),
                        camera.getProtocol(),
                        camera.getStreamUrl()
                    ));
                }
            }

            if (allowedCameraIds != null) {
                playlist = playlist.stream()
                    .filter(camera -> allowedCameraIds.contains(camera.id()))
                    .toList();
            }

            if (!playlist.isEmpty()) {
                responseTiles.add(new VideoWallTileResponse(
                    tile.getId(),
                    tile.getPosition(),
                    tile.getRowSpan(),
                    tile.getColSpan(),
                    tile.getRotationSeconds(),
                    playlist
                ));
            }
        }

        responseTiles = responseTiles.stream()
            .sorted((a, b) -> Integer.compare(
                Objects.requireNonNullElse(a.position(), 0),
                Objects.requireNonNullElse(b.position(), 0)
            ))
            .collect(Collectors.toList());

        return new VideoWallResponse(wall.getId(), wall.getName(), wall.getDescription(), responseTiles);
    }
}
