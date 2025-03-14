package com.jerry.ff.service.impl;

import com.jerry.ff.exception.BusinessException;
import com.jerry.ff.exception.ResourceNotFoundException;
import com.jerry.ff.model.dto.UpdatePasswordDTO;
import com.jerry.ff.model.dto.UpdateUserInfoDTO;
import com.jerry.ff.model.entity.Role;
import com.jerry.ff.model.entity.User;
import com.jerry.ff.model.vo.UserVO;
import com.jerry.ff.repository.UserRepository;
import com.jerry.ff.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserVO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("用户不存在，ID: " + id));
        
        return convertToUserVO(user);
    }

    @Override
    public UserVO getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("用户不存在: " + username));
        
        return convertToUserVO(user);
    }

    @Override
    public Page<UserVO> getAllUsers(Pageable pageable) {
        Page<User> userPage = userRepository.findAll(pageable);
        
        List<UserVO> userVOs = userPage.getContent().stream()
                .map(this::convertToUserVO)
                .collect(Collectors.toList());
        
        return new PageImpl<>(userVOs, pageable, userPage.getTotalElements());
    }

    @Override
    @Transactional
    public UserVO updateUserInfo(String username, UpdateUserInfoDTO updateUserInfoDTO) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("用户不存在: " + username));
        
        // 检查邮箱是否已被其他用户使用
        if (updateUserInfoDTO.getEmail() != null && 
                !updateUserInfoDTO.getEmail().equals(user.getEmail()) && 
                userRepository.existsByEmail(updateUserInfoDTO.getEmail())) {
            throw new BusinessException(400, "邮箱已被注册");
        }
        
        if (updateUserInfoDTO.getEmail() != null) {
            user.setEmail(updateUserInfoDTO.getEmail());
        }
        
        if (updateUserInfoDTO.getPhone() != null) {
            user.setPhone(updateUserInfoDTO.getPhone());
        }
        
        if (updateUserInfoDTO.getAvatar() != null) {
            user.setAvatar(updateUserInfoDTO.getAvatar());
        }
        
        user.setUpdateAt(LocalDateTime.now());
        User updatedUser = userRepository.save(user);
        
        return convertToUserVO(updatedUser);
    }

    @Override
    @Transactional
    public void updatePassword(String username, UpdatePasswordDTO updatePasswordDTO) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("用户不存在: " + username));
        
        // 验证原密码
        if (!passwordEncoder.matches(updatePasswordDTO.getOldPassword(), user.getPassword())) {
            throw new BusinessException(400, "原密码不正确");
        }
        
        // 更新密码
        user.setPassword(passwordEncoder.encode(updatePasswordDTO.getNewPassword()));
        user.setUpdateAt(LocalDateTime.now());
        userRepository.save(user);
    }

    @Override
    @Transactional
    public UserVO updateUserStatus(Long id, Integer status) {
        if (status != 0 && status != 1) {
            throw new BusinessException(400, "用户状态值无效，应为0(禁用)或1(启用)");
        }
        
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("用户不存在，ID: " + id));
        
        user.setStatus(status);
        user.setUpdateAt(LocalDateTime.now());
        User updatedUser = userRepository.save(user);
        
        return convertToUserVO(updatedUser);
    }
    
    private UserVO convertToUserVO(User user) {
        return UserVO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .phone(user.getPhone())
                .avatar(user.getAvatar())
                .status(user.getStatus())
                .memberLevel(user.getMemberLevel())
                .roles(user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                .registerTime(user.getRegisterTime())
                .lastLoginTime(user.getLastLoginTime())
                .build();
    }
} 