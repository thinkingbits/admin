package com.jerry.ff.model.vo;

import com.jerry.ff.model.entity.Anime;
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
public class AnimeVO implements Serializable {
    private Long id;
    private String title;
    private String originalTitle;
    private String description;
    private String posterUrl;
    private String bannerUrl;
    private LocalDate releaseDate;
    private LocalDate endDate;
    private Integer totalEpisodes;
    private Integer currentEpisodes;
    private Double rating;
    private Integer ratingCount;
    private Long categoryId;
    private String categoryName;
    private String region;
    private String language;
    private Anime.AnimeType type;
    private String studio;
    private String season;
    private String broadcast;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private List<AnimeEpisodeVO> episodes;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AnimeEpisodeVO implements Serializable {
        private Long id;
        private String title;
        private Integer episodeNumber;
        private Integer duration;
        private String videoUrl;
        private String thumbnailUrl;
        private String description;
        private LocalDate airDate;
        private Long viewCount;
        private Integer status;
        private LocalDateTime createTime;
        private LocalDateTime updateTime;
    }
} 