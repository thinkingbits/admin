package com.jerry.ff.controller;

import com.jerry.ff.model.dto.UpdatePasswordDTO;
import com.jerry.ff.model.dto.UpdateUserInfoDTO;
import com.jerry.ff.model.vo.UserVO;
import com.jerry.ff.service.UserService;
import com.jerry.ff.util.ResponseResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "用户管理", description = "用户相关接口")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "获取当前用户信息", description = "获取当前登录用户的信息")
    public ResponseResult<UserVO> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseResult.success(userService.getUserByUsername(userDetails.getUsername()));
    }

    @PutMapping("/me")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "更新当前用户信息", description = "更新当前登录用户的基本信息")
    public ResponseResult<UserVO> updateCurrentUser(
            @Valid @RequestBody UpdateUserInfoDTO updateUserInfoDTO,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        return ResponseResult.success(userService.updateUserInfo(userDetails.getUsername(), updateUserInfoDTO));
    }

    @PutMapping("/me/password")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "修改密码", description = "修改当前登录用户的密码")
    public ResponseResult<Void> updatePassword(
            @Valid @RequestBody UpdatePasswordDTO updatePasswordDTO,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        userService.updatePassword(userDetails.getUsername(), updatePasswordDTO);
        return ResponseResult.success();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "获取用户信息", description = "管理员获取指定用户信息")
    public ResponseResult<UserVO> getUser(@PathVariable Long id) {
        return ResponseResult.success(userService.getUserById(id));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "获取用户列表", description = "管理员获取所有用户列表")
    public ResponseResult<Page<UserVO>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        return ResponseResult.success(userService.getAllUsers(pageable));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "更新用户状态", description = "管理员更新用户状态（启用/禁用）")
    public ResponseResult<UserVO> updateUserStatus(
            @PathVariable Long id,
            @RequestParam Integer status) {
        
        return ResponseResult.success(userService.updateUserStatus(id, status));
    }
} 