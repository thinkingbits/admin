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
public class FilmVO implements Serializable {

    private Long id;
    private String title;
    private String originalTitle;
    private String description;
    private String region;
    private String language;
    private LocalDate releaseDate;
    private Integer duration;
    private Integer type;
    private String posterUrl;
    private String bannerUrl;
    private String videoUrl;
    private LocalDate airDate;
    private Double rating;
    private Long categoryId;
    private String categoryName;
    private Integer status;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private List<DirectorVO> directors;
    private List<ActorVO> actors;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DirectorVO implements Serializable {
        private Long id;
        private String name;
    }
} 