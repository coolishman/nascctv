package com.nascctv.mapper;

import com.nascctv.model.CameraStream;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CameraStreamMapper {
    @Select("""
        SELECT id, camera_id, name, stream_type, stream_url, codec, bitrate_kbps, created_at
        FROM camera_streams
        WHERE camera_id = #{cameraId}
        ORDER BY id
        """)
    List<CameraStream> findByCameraId(Long cameraId);

    @Insert("""
        INSERT INTO camera_streams (camera_id, name, stream_type, stream_url, codec, bitrate_kbps)
        VALUES (#{cameraId}, #{name}, #{streamType}, #{streamUrl}, #{codec}, #{bitrateKbps})
        """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(CameraStream stream);
}
