package com.jerry.ff.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeasonVO implements Serializable {
    private Long id;
    private Long tvSeriesId;
    private String title;
    private Long mediaId;
    private String description;
    private String overview;
    private Integer seasonNumber;
    private Integer episodeCount;
    private LocalDate releaseDate;
    private LocalDate endDate;
    private String posterUrl;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private List<EpisodeVO> episodes;
} 