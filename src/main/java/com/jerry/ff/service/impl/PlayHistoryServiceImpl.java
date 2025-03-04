package com.jerry.ff.service.impl;

import com.jerry.ff.exception.BusinessException;
import com.jerry.ff.exception.ResourceNotFoundException;
import com.jerry.ff.model.dto.PlayHistoryDTO;
import com.jerry.ff.model.entity.Movie;
import com.jerry.ff.model.entity.PlayHistory;
import com.jerry.ff.model.entity.User;
import com.jerry.ff.model.vo.PlayHistoryVO;
import com.jerry.ff.repository.MovieRepository;
import com.jerry.ff.repository.PlayHistoryRepository;
import com.jerry.ff.repository.UserRepository;
import com.jerry.ff.service.PlayHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlayHistoryServiceImpl implements PlayHistoryService {

    private final PlayHistoryRepository playHistoryRepository;
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;

    @Override
    @Transactional
    public PlayHistoryVO addPlayHistory(String username, PlayHistoryDTO playHistoryDTO) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("用户不存在"));
        
        Movie movie = movieRepository.findById(playHistoryDTO.getMovieId())
                .orElseThrow(() -> new ResourceNotFoundException("电影不存在，ID: " + playHistoryDTO.getMovieId()));
        
        // 查找是否有该用户的该电影的播放记录，如果有则更新，没有则创建
        PlayHistory history = playHistoryRepository.findByUserIdAndMovieId(user.getId(), movie.getId())
                .orElse(PlayHistory.builder()
                        .user(user)
                        .movie(movie)
                        .build());
        
        history.setDuration(playHistoryDTO.getDuration());
        history.setPlayPosition(playHistoryDTO.getProgress());
        history.setLastPlayTime(LocalDateTime.now());
        
        PlayHistory savedHistory = playHistoryRepository.save(history);
        
        return convertToPlayHistoryVO(savedHistory);
    }

    @Override
    public Page<PlayHistoryVO> getPlayHistory(String username, Pageable pageable) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("用户不存在"));
        
        Page<PlayHistory> historyPage = playHistoryRepository.findByUserIdOrderByLastPlayTimeDesc(user.getId(), pageable);
        
        List<PlayHistoryVO> historyVOs = historyPage.getContent().stream()
                .map(this::convertToPlayHistoryVO)
                .collect(Collectors.toList());
        
        return new PageImpl<>(historyVOs, pageable, historyPage.getTotalElements());
    }

    @Override
    @Transactional
    public void deletePlayHistory(Long id, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("用户不存在"));
        
        PlayHistory history = playHistoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("播放历史记录不存在，ID: " + id));
        
        // 验证是否是该用户的播放记录
        if (!history.getUser().getId().equals(user.getId())) {
            throw new BusinessException(403, "无权操作此播放记录");
        }
        
        playHistoryRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void clearPlayHistory(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("用户不存在"));
        
        playHistoryRepository.deleteByUserId(user.getId());
    }
    
    private PlayHistoryVO convertToPlayHistoryVO(PlayHistory history) {
        return PlayHistoryVO.builder()
                .id(history.getId())
                .movieId(history.getMovie().getId())
                .movieTitle(history.getMovie().getTitle())
                .posterUrl(history.getMovie().getPosterUrl())
                .duration(history.getDuration())
                .progress(history.getPlayPosition())
                .playTime(history.getLastPlayTime())
                .build();
    }
} 