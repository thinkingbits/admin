package com.jerry.ff.service.impl;

import com.jerry.ff.model.dto.CommentDTO;
import com.jerry.ff.model.entity.Comment;
import com.jerry.ff.model.entity.Episode;
import com.jerry.ff.model.entity.Film;
import com.jerry.ff.model.entity.User;
import com.jerry.ff.model.vo.CommentVO;
import com.jerry.ff.repository.CommentRepository;
import com.jerry.ff.repository.EpisodeRepository;
import com.jerry.ff.repository.FilmRepository;
import com.jerry.ff.repository.UserRepository;
import com.jerry.ff.service.CommentService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final EpisodeRepository episodeRepository;
    private final FilmRepository filmRepository;

    @Override
    public Page<CommentVO> getCommentsByMovie(Long movieId, Pageable pageable) {
        // 这里需要基于Film获取关联的Episode，然后获取评论
        // 简化起见，暂时返回全部评论
        return commentRepository.findAll(pageable)
                .map(this::mapToVO);
    }

    @Override
    public CommentVO addComment(Long movieId, CommentDTO commentDTO, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found with username: " + username));
        
        Film film = filmRepository.findById(movieId)
                .orElseThrow(() -> new EntityNotFoundException("Film not found with id: " + movieId));
        
        // 这里简化处理，假设评论是针对电影的第一集
        Episode episode = episodeRepository.findFirstByFilmIdOrderByEpisodeNumberAsc(film.getId())
                .orElseThrow(() -> new EntityNotFoundException("No episodes found for film: " + movieId));
        
        Comment comment = Comment.builder()
                .content(commentDTO.getContent())
                .user(user)
                .episode(episode)
                .rating(commentDTO.getRating())
                .status(0) // 待审核
                .build();
        
        comment = commentRepository.save(comment);
        
        return mapToVO(comment);
    }

    @Override
    public void deleteComment(Long id) {
        delete(id);
    }

    @Override
    public CommentVO auditComment(Long id, Integer status) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found with id: " + id));
        
        comment.setStatus(status);
        comment = commentRepository.save(comment);
        
        return mapToVO(comment);
    }

    @Override
    public Page<CommentVO> getPendingComments(Pageable pageable) {
        return commentRepository.findByStatus(0, pageable)
                .map(this::mapToVO);
    }

    @Override
    public boolean isCommentOwner(Long id, String username) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found with id: " + id));
        
        return comment.getUser().getUsername().equals(username);
    }

    @Override
    @Transactional
    public CommentVO create(CommentVO commentVO) {
        User user = userRepository.findById(commentVO.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + commentVO.getUserId()));
        
        Episode episode = episodeRepository.findById(commentVO.getEpisodeId())
                .orElseThrow(() -> new EntityNotFoundException("Episode not found with id: " + commentVO.getEpisodeId()));
        
        Comment comment = Comment.builder()
                .content(commentVO.getContent())
                .user(user)
                .episode(episode)
                .rating(commentVO.getRating())
                .status(0) // 待审核
                .build();
        
        comment = commentRepository.save(comment);
        
        return mapToVO(comment);
    }

    @Override
    @Transactional
    public CommentVO update(Long id, CommentVO commentVO) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found with id: " + id));
        
        comment.setContent(commentVO.getContent());
        comment.setRating(commentVO.getRating());
        
        comment = commentRepository.save(comment);
        
        return mapToVO(comment);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!commentRepository.existsById(id)) {
            throw new EntityNotFoundException("Comment not found with id: " + id);
        }
        commentRepository.deleteById(id);
    }

    @Override
    public CommentVO findById(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found with id: " + id));
        
        return mapToVO(comment);
    }

    @Override
    public Page<CommentVO> findAll(Pageable pageable) {
        return commentRepository.findAll(pageable)
                .map(this::mapToVO);
    }

    @Override
    public Page<CommentVO> findByEpisodeId(Long episodeId, Pageable pageable) {
        return commentRepository.findByEpisodeId(episodeId, pageable)
                .map(this::mapToVO);
    }

    @Override
    public Page<CommentVO> findByUserId(Long userId, Pageable pageable) {
        return commentRepository.findByUserId(userId, pageable)
                .map(this::mapToVO);
    }

    @Override
    public Page<CommentVO> findByStatus(Integer status, Pageable pageable) {
        return commentRepository.findByStatus(status, pageable)
                .map(this::mapToVO);
    }

    @Override
    @Transactional
    public CommentVO approve(Long id) {
        return auditComment(id, 1); // 1-通过
    }

    @Override
    @Transactional
    public CommentVO reject(Long id) {
        return auditComment(id, 2); // 2-拒绝
    }
    
    private CommentVO mapToVO(Comment comment) {
        CommentVO vo = new CommentVO();
        BeanUtils.copyProperties(comment, vo);
        
        vo.setUserId(comment.getUser().getId());
        vo.setUserName(comment.getUser().getUsername());
        
        vo.setEpisodeId(comment.getEpisode().getId());
        vo.setEpisodeTitle(comment.getEpisode().getTitle());
        
        return vo;
    }
}