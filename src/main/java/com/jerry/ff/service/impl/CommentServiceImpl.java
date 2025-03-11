package com.jerry.ff.service.impl;

import com.jerry.ff.model.dto.CommentDTO;
import com.jerry.ff.model.vo.CommentVO;
import com.jerry.ff.repository.CommentRepository;
import com.jerry.ff.repository.MovieRepository;
import com.jerry.ff.repository.UserRepository;
import com.jerry.ff.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;

    @Override
    public Page<CommentVO> getCommentsByMovie(Long movieId, Pageable pageable) {
        return null;
    }

    @Override
    public CommentVO addComment(Long movieId, CommentDTO commentDTO, String username) {
        return null;
    }

    @Override
    public void deleteComment(Long id) {

    }

    @Override
    public CommentVO auditComment(Long id, Integer status) {
        return null;
    }

    @Override
    public Page<CommentVO> getPendingComments(Pageable pageable) {
        return null;
    }

    @Override
    public boolean isCommentOwner(Long id, String username) {
        return false;
    }

//    @Override
//    public Page<CommentVO> getCommentsByMovie(Long movieId, Pageable pageable) {
//        Page<Comment> commentPage = commentRepository.findByMovieIdAndStatus(movieId, 1, pageable);
//        return convertToCommentVOPage(commentPage);
//    }
//
//    @Override
//    @Transactional
//    public CommentVO addComment(Long movieId, CommentDTO commentDTO, String username) {
//        User user = userRepository.findByUsername(username)
//                .orElseThrow(() -> new ResourceNotFoundException("用户不存在: " + username));
//
//        Movie movie = movieRepository.findById(movieId)
//                .orElseThrow(() -> new ResourceNotFoundException("电影不存在，ID: " + movieId));
//
//        Comment comment = Comment.builder()
//                .content(commentDTO.getContent())
//                .rating(commentDTO.getRating())
//                .user(user)
//                .media(movie)
//                .status(0) // 默认待审核
//                .build();
//
//        Comment savedComment = commentRepository.save(comment);
//        return convertToCommentVO(savedComment);
//    }
//
//    @Override
//    @Transactional
//    public void deleteComment(Long id) {
//        Comment comment = commentRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("评论不存在，ID: " + id));
//
//        commentRepository.delete(comment);
//    }
//
//    @Override
//    @Transactional
//    public CommentVO auditComment(Long id, Integer status) {
//        if (status != 1 && status != 2) {
//            throw new BusinessException(400, "无效的状态值，应为1(通过)或2(拒绝)");
//        }
//
//        Comment comment = commentRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("评论不存在，ID: " + id));
//
//        comment.setStatus(status);
//        Comment updatedComment = commentRepository.save(comment);
//
//        // 如果评论通过审核，更新电影评分
//        if (status == 1 && comment.getRating() != null) {
//            updateMovieRating(comment.getMedia().getId());
//        }
//
//        return convertToCommentVO(updatedComment);
//    }
//
//    @Override
//    public Page<CommentVO> getPendingComments(Pageable pageable) {
//        Page<Comment> commentPage = commentRepository.findByStatus(0, pageable);
//        return convertToCommentVOPage(commentPage);
//    }
//
//    @Override
//    public boolean isCommentOwner(Long id, String username) {
//        Comment comment = commentRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("评论不存在，ID: " + id));
//
//        return comment.getUser().getUsername().equals(username);
//    }

//    private void updateMovieRating(Long movieId) {
//        Movie movie = movieRepository.findById(movieId)
//                .orElseThrow(() -> new ResourceNotFoundException("电影不存在，ID: " + movieId));
//
//        List<Comment> approvedComments = commentRepository.findByMovieIdAndStatus(movieId, 1,
//                Sort.by(Sort.Direction.DESC, "createTime"));
//
//        if (!approvedComments.isEmpty()) {
//            double averageRating = approvedComments.stream()
//                    .filter(c -> c.getRating() != null)
//                    .mapToInt(Comment::getRating)
//                    .average()
//                    .orElse(0.0);
//
//            movie.setRating(averageRating);
//            movieRepository.save(movie);
//        }
//    }

//    private CommentVO convertToCommentVO(Comment comment) {
//        return CommentVO.builder()
//                .id(comment.getId())
//                .content(comment.getContent())
//                .userId(comment.getUser().getId())
//                .username(comment.getUser().getUsername())
//                .userAvatar(comment.getUser().getAvatar())
//                .movieId(comment.getMedia().getId())
//                .movieTitle(comment.getMedia().getTitle())
//                .rating(comment.getRating())
//                .status(comment.getStatus())
//                .createTime(comment.getCreateTime())
//                .build();
//    }
//
//    private Page<CommentVO> convertToCommentVOPage(Page<Comment> commentPage) {
//        List<CommentVO> commentVOs = commentPage.getContent().stream()
//                .map(this::convertToCommentVO)
//                .collect(Collectors.toList());
//
//        return new PageImpl<>(commentVOs, commentPage.getPageable(), commentPage.getTotalElements());
//    }
} 