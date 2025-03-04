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
public class PlayHistoryVO implements Serializable {
    
    private Long id;
    private Long movieId;
    private String movieTitle;
    private String posterUrl;
    private Integer duration; // 电影总时长，单位：秒
    private Integer progress; // 播放进度，单位：秒
    private LocalDateTime playTime;
} 