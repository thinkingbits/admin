package com.jerry.ff.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActorVO implements Serializable {
    
    private Long id;
    private String name;
    private String originalName;
    private String avatarUrl;
    private String description;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
} 