package com.jerry.ff.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EpisodeVO implements Serializable {
    private Long id;
    private Long seasonId;
    private String title;
    private Integer episodeNumber;
    private Integer duration;
    private String videoUrl;
    private String overview;
    private String thumbnailUrl;
    private String description;
    private String stillUrl;
    private LocalDate airDate;
    private Long viewCount;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
} 