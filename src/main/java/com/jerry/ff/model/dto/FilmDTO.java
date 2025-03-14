package com.jerry.ff.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FilmDTO {

    private Long id;

    private Integer type;

    @NotBlank(message = "电影标题不能为空")
    private String title;

    private String originalTitle;

    private String description;

    private String region;

    private String language;

    @NotNull(message = "发行日期不能为空")
    private LocalDate airDate;

    private String posterUrl;

    private String bannerUrl;

    private String videoUrl;

    @NotNull(message = "分类ID不能为空")
    private Long categoryId;

    private List<Long> directorIds;

    private List<Long> actorIds;

    private Integer status; // 0-下架, 1-上架
} 