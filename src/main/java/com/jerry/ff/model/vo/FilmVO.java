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
    private String description;
    private LocalDate releaseDate;
    private Integer duration;
    private String posterUrl;
    private String videoUrl;
    private Double rating;
    private Long categoryId;
    private String categoryName;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private List<DirectorVO> directors;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DirectorVO implements Serializable {
        private Long id;
        private String name;
    }
} 