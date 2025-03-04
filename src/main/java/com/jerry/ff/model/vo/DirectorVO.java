package com.jerry.ff.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DirectorVO implements Serializable {
    
    private Long id;
    private String name;
    private String biography;
    private String photoUrl;
} 