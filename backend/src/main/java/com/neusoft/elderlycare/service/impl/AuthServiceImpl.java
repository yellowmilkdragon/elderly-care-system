package com.neusoft.elderlycare.service.impl;

import com.neusoft.elderlycare.dto.LoginRequest;
import com.neusoft.elderlycare.service.AuthService;
import com.neusoft.elderlycare.vo.LoginResponse;
import com.neusoft.elderlycare.vo.UserProfile;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthServiceImpl implements AuthService {

    @Override
    public LoginResponse login(LoginRequest request) {
        UserProfile profile = new UserProfile(1L, request.username(), "系统管理员", "ADMIN");
        return new LoginResponse("mock-jwt-token", profile, List.of("dashboard:view", "customer:view"));
    }

    @Override
    public UserProfile currentUser() {
        return new UserProfile(1L, "admin", "系统管理员", "ADMIN");
    }
}
