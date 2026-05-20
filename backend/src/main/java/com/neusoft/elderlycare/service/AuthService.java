package com.neusoft.elderlycare.service;

import com.neusoft.elderlycare.dto.LoginRequest;
import com.neusoft.elderlycare.vo.LoginResponse;
import com.neusoft.elderlycare.vo.UserProfile;

public interface AuthService {
    LoginResponse login(LoginRequest request);
    UserProfile currentUser();
}
