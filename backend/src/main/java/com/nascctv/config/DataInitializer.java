package com.nascctv.config;

import com.nascctv.mapper.CameraMapper;
import com.nascctv.mapper.UserCameraPermissionMapper;
import com.nascctv.mapper.UserMapper;
import com.nascctv.mapper.VideoWallMapper;
import com.nascctv.model.User;
import com.nascctv.model.VideoWall;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    private final UserMapper userMapper;
    private final CameraMapper cameraMapper;
    private final VideoWallMapper videoWallMapper;
    private final UserCameraPermissionMapper userCameraPermissionMapper;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(
        UserMapper userMapper,
        CameraMapper cameraMapper,
        VideoWallMapper videoWallMapper,
        UserCameraPermissionMapper userCameraPermissionMapper,
        PasswordEncoder passwordEncoder
    ) {
        this.userMapper = userMapper;
        this.cameraMapper = cameraMapper;
        this.videoWallMapper = videoWallMapper;
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

        if (videoWallMapper.countWalls() == 0) {
            VideoWall wall = new VideoWall(null, "运营中心电视墙", "默认电视墙布局", admin.getId(), null);
            videoWallMapper.insert(wall);
        }

        if (admin.getId() != null) {
            cameraMapper.findAll().forEach(camera ->
                userCameraPermissionMapper.insertPermission(admin.getId(), camera.getId())
            );
        }
    }
}
