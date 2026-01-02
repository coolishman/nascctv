package com.nascctv.config;

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
import com.nascctv.model.WallTileChannel;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {
    private final UserMapper userMapper;
    private final CameraMapper cameraMapper;
    private final VideoWallMapper videoWallMapper;
    private final WallTileMapper wallTileMapper;
    private final WallTileChannelMapper wallTileChannelMapper;
    private final UserCameraPermissionMapper userCameraPermissionMapper;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(
        UserMapper userMapper,
        CameraMapper cameraMapper,
        VideoWallMapper videoWallMapper,
        WallTileMapper wallTileMapper,
        WallTileChannelMapper wallTileChannelMapper,
        UserCameraPermissionMapper userCameraPermissionMapper,
        PasswordEncoder passwordEncoder
    ) {
        this.userMapper = userMapper;
        this.cameraMapper = cameraMapper;
        this.videoWallMapper = videoWallMapper;
        this.wallTileMapper = wallTileMapper;
        this.wallTileChannelMapper = wallTileChannelMapper;
        this.userCameraPermissionMapper = userCameraPermissionMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        User admin = userMapper.findByUsername("admin").orElseGet(() -> {
            User user = new User(null, "admin", passwordEncoder.encode("admi"), "ADMIN", null, null);
            userMapper.insert(user);
            return user;
        });

        if (cameraMapper.countCameras() == 0) {
            List<Camera> cameras = List.of(
                new Camera(null, "园区南门高清枪机", "ONVIF", "Hikvision", "DS-2CD2T", "DIGEST",
                    "rtsp://10.0.0.12/stream1", "online", "南门出入口", null),
                new Camera(null, "停车场球机", "RTSP", "Dahua", "DH-SD", "BASIC",
                    "rtsp://10.0.0.21/stream1", "offline", "地下停车场", null),
                new Camera(null, "仓库全景", "GB/T 28181", "通用设备", "GB28181", "TOKEN",
                    "sip:34020000001320000001@10.0.0.30", "online", "仓储区", null),
                new Camera(null, "办公楼大厅", "RTSP", "Uniview", "IPC-01", "BASIC",
                    "rtsp://10.0.0.31/stream1", "online", "办公楼一层", null)
            );
            cameras.forEach(cameraMapper::insert);
        }

        if (videoWallMapper.countWalls() == 0) {
            VideoWall wall = new VideoWall(null, "运营中心电视墙", "默认电视墙布局", admin.getId(), null);
            videoWallMapper.insert(wall);

            List<Camera> cameras = cameraMapper.findAll();
            if (!cameras.isEmpty()) {
                WallTile tileOne = new WallTile(null, wall.getId(), cameras.get(0).getId(), 1, 1, 1, 8, null);
                WallTile tileTwo = new WallTile(null, wall.getId(), cameras.get(1).getId(), 2, 1, 1, 12, null);
                WallTile tileThree = new WallTile(null, wall.getId(), cameras.get(2).getId(), 3, 1, 1, 10, null);
                wallTileMapper.insert(tileOne);
                wallTileMapper.insert(tileTwo);
                wallTileMapper.insert(tileThree);

                wallTileChannelMapper.insert(new WallTileChannel(null, tileOne.getId(), cameras.get(0).getId(), 1));
                wallTileChannelMapper.insert(new WallTileChannel(null, tileOne.getId(), cameras.get(1).getId(), 2));
                wallTileChannelMapper.insert(new WallTileChannel(null, tileTwo.getId(), cameras.get(2).getId(), 1));
                wallTileChannelMapper.insert(new WallTileChannel(null, tileThree.getId(), cameras.get(3).getId(), 1));
            }
        }

        if (admin.getId() != null) {
            cameraMapper.findAll().forEach(camera ->
                userCameraPermissionMapper.insertPermission(admin.getId(), camera.getId())
            );
        }
    }
}
