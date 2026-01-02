package com.nascctv.mapper;

import com.nascctv.dto.CameraPreview;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface WallTileChannelMapper {
    @Select("""
        SELECT wtc.tile_id AS tileId,
               c.id AS id,
               c.name AS name,
               c.protocol AS protocol,
               c.stream_url AS streamUrl
        FROM wall_tile_channels wtc
        JOIN cameras c ON c.id = wtc.camera_id
        JOIN wall_tiles wt ON wt.id = wtc.tile_id
        WHERE wt.wall_id = #{wallId}
        ORDER BY wtc.tile_id, wtc.order_index
        """)
    List<WallTileChannelView> findChannelsByWallId(Long wallId);

    record WallTileChannelView(
        Long tileId,
        Long id,
        String name,
        String protocol,
        String streamUrl
    ) {
        public CameraPreview toPreview() {
            return new CameraPreview(id, name, protocol, streamUrl);
        }
    }
}
