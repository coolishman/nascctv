package com.nascctv.mapper;

import org.apache.ibatis.annotations.Mapper;
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
}
