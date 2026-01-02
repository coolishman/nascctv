package com.nascctv.mapper;

import com.nascctv.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Optional;

@Mapper
public interface UserMapper {
    @Select("""
        SELECT id, username, password_hash, role, last_login_ip, created_at
        FROM users
        WHERE username = #{username}
        """)
    Optional<User> findByUsername(String username);

    @Select("""
        SELECT id, username, password_hash, role, last_login_ip, created_at
        FROM users
        ORDER BY id
        """)
    List<User> findAll();

    @Select("SELECT COUNT(*) FROM users")
    int countUsers();

    @Insert("""
        INSERT INTO users (username, password_hash, role, last_login_ip)
        VALUES (#{username}, #{passwordHash}, #{role}, #{lastLoginIp})
        """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(User user);

    @Update("""
        UPDATE users
        SET last_login_ip = #{lastLoginIp}
        WHERE id = #{id}
        """)
    int updateLastLoginIp(User user);
}
