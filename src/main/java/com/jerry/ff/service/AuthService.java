package com.jerry.ff.service;

import com.jerry.ff.model.dto.LoginRequest;
import com.jerry.ff.model.dto.RegisterRequest;
import com.jerry.ff.model.vo.JwtResponse;
import com.jerry.ff.model.vo.UserVO;

public interface AuthService {
    
    JwtResponse login(LoginRequest loginRequest);
    
    UserVO register(RegisterRequest registerRequest);

    JwtResponse refreshToken(String token);
} 