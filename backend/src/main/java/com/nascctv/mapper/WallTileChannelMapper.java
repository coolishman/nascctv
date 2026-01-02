package com.nascctv.mapper;

import com.nascctv.dto.CameraPreview;
import com.nascctv.model.WallTileChannel;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
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

    @Insert("""
        INSERT INTO wall_tile_channels (tile_id, camera_id, order_index)
        VALUES (#{tileId}, #{cameraId}, #{orderIndex})
        """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(WallTileChannel channel);

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
