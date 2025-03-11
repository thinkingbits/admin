package com.jerry.ff.model.dto;

import jakarta.validation.constraints.Min;
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

    @NotBlank(message = "电影标题不能为空")
    private String title;

    private String description;

    @NotNull(message = "发行日期不能为空")
    private LocalDate releaseDate;

    @NotNull(message = "时长不能为空")
    @Min(value = 1, message = "时长必须大于0")
    private Integer duration;

    private String posterUrl;

    private String videoUrl;

    @NotNull(message = "分类ID不能为空")
    private Long categoryId;

    private List<Long> directorIds;

    private Integer status; // 0-下架, 1-上架
} 