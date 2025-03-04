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
public class DramaVO implements Serializable {
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
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private List<SeasonVO> seasons;
    private List<ActorVO> actors;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ActorVO implements Serializable {
        private Long id;
        private String name;
        private String originalName;
        private String avatarUrl;
        private String roleName;
        private Integer roleType;
    }
} 