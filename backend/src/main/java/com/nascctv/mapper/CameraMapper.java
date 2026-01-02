package com.nascctv.mapper;

import com.nascctv.model.Camera;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Optional;

@Mapper
public interface CameraMapper {
    @Select("""
        SELECT id, name, protocol, stream_url, status, location, created_at
        FROM cameras
        ORDER BY id
        """)
    List<Camera> findAll();

    @Select("""
        SELECT id, name, protocol, stream_url, status, location, created_at
        FROM cameras
        WHERE id = #{id}
        """)
    Optional<Camera> findById(Long id);

    @Insert("""
        INSERT INTO cameras (name, protocol, stream_url, status, location)
        VALUES (#{name}, #{protocol}, #{streamUrl}, #{status}, #{location})
        """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Camera camera);

    @Update("""
        UPDATE cameras
        SET name = #{name},
            protocol = #{protocol},
            stream_url = #{streamUrl},
            status = #{status},
            location = #{location}
        WHERE id = #{id}
        """)
    int update(Camera camera);

    @Delete("DELETE FROM cameras WHERE id = #{id}")
    int delete(Long id);
}
