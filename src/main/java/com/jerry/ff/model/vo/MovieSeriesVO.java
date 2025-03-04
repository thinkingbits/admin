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
public class MovieSeriesVO implements Serializable {
    private Long id;
    private String title;
    private String originalTitle;
    private String description;
    private String posterUrl;
    private String bannerUrl;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer totalMovies;
    private Double rating;
    private Integer ratingCount;
    private Long categoryId;
    private String categoryName;
    private String region;
    private String language;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private List<MovieInSeriesVO> movies;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MovieInSeriesVO implements Serializable {
        private Long id;
        private String title;
        private String description;
        private LocalDate releaseDate;
        private Integer duration;
        private String posterUrl;
        private Double rating;
        private Integer seriesOrder;
    }
} 