package com.nascctv.service;

import com.nascctv.dto.AuthResponse;
import com.nascctv.dto.LoginRequest;
import com.nascctv.dto.RegisterRequest;
import com.nascctv.mapper.UserMapper;
import com.nascctv.model.User;
import com.nascctv.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public AuthService(
        AuthenticationManager authenticationManager,
        JwtService jwtService,
        PasswordEncoder passwordEncoder,
        UserMapper userMapper
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );
        User user = userMapper.findByUsername(authentication.getName())
            .orElseThrow(() -> new IllegalStateException("User not found"));
        String token = jwtService.generateToken(user.username(), user.role());
        return new AuthResponse(token, "Bearer");
    }

    public AuthResponse register(RegisterRequest request) {
        User user = new User(null, request.username(), passwordEncoder.encode(request.password()), "USER", null);
        userMapper.insert(user);
        String token = jwtService.generateToken(user.username(), user.role());
        return new AuthResponse(token, "Bearer");
    }
}
