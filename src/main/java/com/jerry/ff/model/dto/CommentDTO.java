package com.jerry.ff.model.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
    
    @NotBlank(message = "评论内容不能为空")
    private String content;
    
    @Min(value = 1, message = "评分最小为1")
    @Max(value = 10, message = "评分最大为10")
    private Integer rating;
} 