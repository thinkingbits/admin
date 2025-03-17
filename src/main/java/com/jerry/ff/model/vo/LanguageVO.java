package com.jerry.ff.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LanguageVO {
    private Long id;
    private String name;
    private String code;
    private String description;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
} 