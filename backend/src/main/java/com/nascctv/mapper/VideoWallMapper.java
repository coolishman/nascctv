package com.nascctv.mapper;

import com.nascctv.model.VideoWall;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
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

    @Select("SELECT COUNT(*) FROM video_walls")
    int countWalls();

    @Insert("""
        INSERT INTO video_walls (name, description, created_by)
        VALUES (#{name}, #{description}, #{createdBy})
        """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(VideoWall wall);
}
