package com.jerry.ff.service.impl;

import com.jerry.ff.exception.BusinessException;
import com.jerry.ff.model.dto.LoginRequest;
import com.jerry.ff.model.dto.RegisterRequest;
import com.jerry.ff.model.entity.Role;
import com.jerry.ff.model.entity.User;
import com.jerry.ff.model.vo.JwtResponse;
import com.jerry.ff.model.vo.UserVO;
import com.jerry.ff.repository.RoleRepository;
import com.jerry.ff.repository.UserRepository;
import com.jerry.ff.security.JwtTokenProvider;
import com.jerry.ff.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public JwtResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);
        
        org.springframework.security.core.userdetails.UserDetails userDetails = 
                (org.springframework.security.core.userdetails.UserDetails) authentication.getPrincipal();
        
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(role -> role.replace("ROLE_", ""))
                .collect(Collectors.toList());
        
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        user.setLastLoginTime(LocalDateTime.now());
        userRepository.save(user);
        
        return JwtResponse.builder()
                .token(jwt)
                .tokenType("Bearer")
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .avatar(user.getAvatar())
                .roles(roles)
                .memberLevel(user.getMemberLevel())
                .build();
    }

    @Override
    @Transactional
    public UserVO register(RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new BusinessException(400, "用户名已存在");
        }

        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new BusinessException(400, "邮箱已被注册");
        }

        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new BusinessException(500, "系统错误：用户角色不存在"));

        Set<Role> roles = new HashSet<>();
        roles.add(userRole);

        User user = User.builder()
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .email(registerRequest.getEmail())
                .phone(registerRequest.getPhone())
                .roles(roles)
                .status(1)
                .memberLevel(0)
                .registerTime(LocalDateTime.now())
                .build();

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
} 