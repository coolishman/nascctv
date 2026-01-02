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
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

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

    public AuthResponse login(LoginRequest request, String loginIp) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );
        User user = userMapper.findByUsername(authentication.getName())
            .orElseThrow(() -> new IllegalStateException("User not found"));
        String previousLoginIp = user.getLastLoginIp();
        user.setLastLoginIp(loginIp);
        userMapper.updateLastLoginIp(user);
        String token = jwtService.generateToken(user.getUsername(), user.getRole());
        return new AuthResponse(token, "Bearer", previousLoginIp);
    }

    public AuthResponse register(RegisterRequest request) {
        if (userMapper.findByUsername(request.username()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists.");
        }
        User user = new User(null, request.username(), passwordEncoder.encode(request.password()), "USER", null, null);
        userMapper.insert(user);
        String token = jwtService.generateToken(user.getUsername(), user.getRole());
        return new AuthResponse(token, "Bearer", null);
    }
}
