package com.jerry.ff.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteVO {
    private Long id;
    private Integer status;
    private Long userId;
    private String userName;
    private Long episodeId;
    private String episodeTitle;
    private LocalDateTime createAt;
} 