package com.jerry.ff.service;

import com.jerry.ff.model.dto.DirectorDTO;
import com.jerry.ff.model.vo.DirectorVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DirectorService {
    
    Page<DirectorVO> getAllDirectors(Pageable pageable);
    
    DirectorVO getDirector(Long id);
    
    DirectorVO createDirector(DirectorDTO directorDTO);
    
    DirectorVO updateDirector(Long id, DirectorDTO directorDTO);
    
    void deleteDirector(Long id);
    
    List<DirectorVO> getPopularDirectors(int limit);
} 