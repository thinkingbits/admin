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
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        return convertToUserVO(user);
    }

    @Override
    public UserVO getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
        
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
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
        
        // 检查邮箱是否已被其他用户使用
        if (updateUserInfoDTO.getEmail() != null && 
                !updateUserInfoDTO.getEmail().equals(user.getEmail()) && 
                userRepository.existsByEmail(updateUserInfoDTO.getEmail())) {
            throw new BusinessException(400, "Email already registered");
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
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
        
        // 验证原密码
        if (!passwordEncoder.matches(updatePasswordDTO.getOldPassword(), user.getPassword())) {
            throw new BusinessException(400, "Incorrect old password");
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
            throw new BusinessException(400, "Invalid user status, must be 0(disabled) or 1(enabled)");
        }
        
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        user.setStatus(status);
        user.setUpdateAt(LocalDateTime.now());
        User updatedUser = userRepository.save(user);
        
        return convertToUserVO(updatedUser);
    }
    
    @Transactional
    public UserVO updateUser(Long id, UserVO userVO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException(404, "User not found"));

        if (userVO.getEmail() != null && !userVO.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(userVO.getEmail())) {
                throw new BusinessException(400, "Email already registered");
            }
            user.setEmail(userVO.getEmail());
        }

        if (userVO.getPhone() != null) {
            user.setPhone(userVO.getPhone());
        }

        if (userVO.getAvatar() != null) {
            user.setAvatar(userVO.getAvatar());
        }

        if (userVO.getStatus() != null) {
            if (userVO.getStatus() != 0 && userVO.getStatus() != 1) {
                throw new BusinessException(400, "Invalid user status, must be 0(disabled) or 1(enabled)");
            }
            user.setStatus(userVO.getStatus());
        }

        User savedUser = userRepository.save(user);

        return UserVO.builder()
                .id(savedUser.getId())
                .username(savedUser.getUsername())
                .email(savedUser.getEmail())
                .phone(savedUser.getPhone())
                .avatar(savedUser.getAvatar())
                .status(savedUser.getStatus())
                .memberLevel(savedUser.getMemberLevel())
                .roles(savedUser.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                .registerTime(savedUser.getRegisterTime())
                .lastLoginTime(savedUser.getLastLoginTime())
                .build();
    }

    @Transactional
    public void updatePassword(Long id, String oldPassword, String newPassword) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException(404, "User not found"));

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new BusinessException(400, "Incorrect old password");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
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