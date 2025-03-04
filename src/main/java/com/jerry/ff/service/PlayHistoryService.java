package com.jerry.ff.service;

import com.jerry.ff.model.dto.PlayHistoryDTO;
import com.jerry.ff.model.vo.PlayHistoryVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PlayHistoryService {
    
    PlayHistoryVO addPlayHistory(String username, PlayHistoryDTO playHistoryDTO);
    
    Page<PlayHistoryVO> getPlayHistory(String username, Pageable pageable);
    
    void deletePlayHistory(Long id, String username);
    
    void clearPlayHistory(String username);
} 