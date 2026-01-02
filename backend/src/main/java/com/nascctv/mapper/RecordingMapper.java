package com.nascctv.mapper;

import com.nascctv.model.Recording;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RecordingMapper {
    @Select("""
        SELECT id, camera_id, storage_path, format, size_bytes, started_at, ended_at, retention_days, created_at
        FROM recordings
        WHERE camera_id = #{cameraId}
        ORDER BY started_at DESC
        """)
    List<Recording> findByCameraId(Long cameraId);

    @Insert("""
        INSERT INTO recordings (camera_id, storage_path, format, size_bytes, started_at, ended_at, retention_days)
        VALUES (#{cameraId}, #{storagePath}, #{format}, #{sizeBytes}, #{startedAt}, #{endedAt}, #{retentionDays})
        """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Recording recording);
}
