package com.jerry.ff.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileUploadResponse implements Serializable {
    
    private String url;
    private long size;
} 