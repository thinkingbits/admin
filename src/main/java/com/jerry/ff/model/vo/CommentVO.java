package com.jerry.ff.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentVO implements Serializable{
    
    private Long id;
    private String content;
    private Long userId;
    private String username;
    private String userAvatar;
    private Long movieId;
    private String movieTitle;
    private Integer rating;
    private Integer status;
    private LocalDateTime createTime;
} 