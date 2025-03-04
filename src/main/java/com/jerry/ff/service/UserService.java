package com.jerry.ff.service;

import com.jerry.ff.model.dto.UpdatePasswordDTO;
import com.jerry.ff.model.dto.UpdateUserInfoDTO;
import com.jerry.ff.model.vo.UserVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    
    UserVO getUserById(Long id);
    
    UserVO getUserByUsername(String username);
    
    Page<UserVO> getAllUsers(Pageable pageable);
    
    UserVO updateUserInfo(String username, UpdateUserInfoDTO updateUserInfoDTO);
    
    void updatePassword(String username, UpdatePasswordDTO updatePasswordDTO);
    
    UserVO updateUserStatus(Long id, Integer status);
} 