package com.jerry.ff.controller;

import com.jerry.ff.model.dto.LoginRequest;
import com.jerry.ff.model.dto.RegisterRequest;
import com.jerry.ff.model.vo.JwtResponse;
import com.jerry.ff.model.vo.UserVO;
import com.jerry.ff.service.AuthService;
import com.jerry.ff.util.ResponseResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "认证管理", description = "用户登录注册相关接口")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "用户名密码登录获取JWT令牌")
    public ResponseResult<JwtResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseResult.success(authService.login(loginRequest));
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "用户注册", description = "注册新用户")
    public ResponseResult<UserVO> register(@Valid @RequestBody RegisterRequest registerRequest) {
        return ResponseResult.success(authService.register(registerRequest));
    }
} 