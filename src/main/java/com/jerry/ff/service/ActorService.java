package com.jerry.ff.service;

import com.jerry.ff.model.dto.ActorDTO;
import com.jerry.ff.model.vo.ActorVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ActorService {
    
    ActorVO createActor(ActorDTO actorDTO);
    
    ActorVO updateActor(Long id, ActorDTO actorDTO);
    
    void deleteActor(Long id);
    
    ActorVO getActor(Long id);
    
    Page<ActorVO> getAllActors(Pageable pageable);
    
    Page<ActorVO> searchActors(String keyword, Pageable pageable);
    
    List<ActorVO> getActorsByFilm(Long filmId);
    
    List<ActorVO> getActorsByIds(List<Long> ids);
} 