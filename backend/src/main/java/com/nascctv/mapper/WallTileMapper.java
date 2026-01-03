package com.nascctv.mapper;

import com.nascctv.model.WallTile;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Delete;

import java.util.List;

@Mapper
public interface WallTileMapper {
    @Select("""
        SELECT id, wall_id, camera_id, position, row_span, col_span, rotation_seconds, enabled, created_at
        FROM wall_tiles
        WHERE wall_id = #{wallId}
        ORDER BY position
        """)
    List<WallTile> findByWallId(Long wallId);

    @Insert("""
        INSERT INTO wall_tiles (wall_id, camera_id, position, row_span, col_span, rotation_seconds, enabled)
        VALUES (#{wallId}, #{cameraId}, #{position}, #{rowSpan}, #{colSpan}, #{rotationSeconds}, #{enabled})
        """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(WallTile tile);

    @Delete("DELETE FROM wall_tiles WHERE wall_id = #{wallId}")
    int deleteByWallId(Long wallId);
}
