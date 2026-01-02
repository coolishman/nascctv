package com.nascctv.mapper;

import com.nascctv.model.WallTile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface WallTileMapper {
    @Select("""
        SELECT id, wall_id, camera_id, position, row_span, col_span, rotation_seconds, created_at
        FROM wall_tiles
        WHERE wall_id = #{wallId}
        ORDER BY position
        """)
    List<WallTile> findByWallId(Long wallId);
}
