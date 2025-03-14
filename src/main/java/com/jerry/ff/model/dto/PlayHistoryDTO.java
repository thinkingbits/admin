package com.jerry.ff.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlayHistoryDTO {
    
    @NotNull(message = "电影ID不能为空")
    private Long episodeId;
    
    private Integer duration; // 播放时长，单位：秒
    
    private Integer progress; // 播放进度，单位：秒
} 