package com.nascctv.mapper;

import com.nascctv.model.SystemSettings;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Optional;

@Mapper
public interface SystemSettingsMapper {
    @Select("""
        SELECT id, retention_days, alert_sound, auto_rotate, updated_at
        FROM system_settings
        WHERE id = #{id}
        """)
    Optional<SystemSettings> findById(Long id);

    @Insert("""
        INSERT INTO system_settings (retention_days, alert_sound, auto_rotate)
        VALUES (#{retentionDays}, #{alertSound}, #{autoRotate})
        """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(SystemSettings settings);

    @Update("""
        UPDATE system_settings
        SET retention_days = #{retentionDays},
            alert_sound = #{alertSound},
            auto_rotate = #{autoRotate}
        WHERE id = #{id}
        """)
    int update(SystemSettings settings);
}
