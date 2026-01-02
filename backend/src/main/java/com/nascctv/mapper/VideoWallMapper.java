package com.nascctv.mapper;

import com.nascctv.model.VideoWall;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Optional;

@Mapper
public interface VideoWallMapper {
    @Select("""
        SELECT id, name, description, created_by, created_at
        FROM video_walls
        WHERE id = #{id}
        """)
    Optional<VideoWall> findById(Long id);
}
