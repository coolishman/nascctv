package com.nascctv.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserCameraPermissionMapper {
    @Select("""
        SELECT camera_id
        FROM user_camera_permissions
        WHERE user_id = #{userId}
        """)
    List<Long> findCameraIdsByUserId(Long userId);

    @Insert("""
        INSERT IGNORE INTO user_camera_permissions (user_id, camera_id)
        VALUES (#{userId}, #{cameraId})
        """)
    @Options(useGeneratedKeys = false)
    int insertPermission(@Param("userId") Long userId, @Param("cameraId") Long cameraId);
}
