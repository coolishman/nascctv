package com.nascctv.service;

import com.nascctv.mapper.UserMapper;
import com.nascctv.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(String username, String password) {
        User user = new User(null, username, passwordEncoder.encode(password), "USER", null);
        userMapper.insert(user);
        return user;
    }

    public List<User> listUsers() {
        return userMapper.findAll();
    }
}
