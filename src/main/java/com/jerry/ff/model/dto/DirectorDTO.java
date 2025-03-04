package com.jerry.ff.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DirectorDTO {
    
    @NotBlank(message = "导演姓名不能为空")
    @Size(min = 2, max = 50, message = "导演姓名长度必须在2-50个字符之间")
    private String name;
    
    private String biography;
    
    private String photoUrl;
} 