package com.jerry.ff.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Table(name = "animes")
@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Anime extends Media {
    
    private Integer totalEpisodes;
    private Integer currentEpisodes;
    
    @Enumerated(EnumType.STRING)
    private AnimeType type;  // TV, MOVIE, OVA, ONA
    
    private String studio;  // 制作公司
    private String season;  // 播出季度，如：2024年春季
    private String broadcast;  // 播出时间，如：每周日 23:00
    
    @OneToMany(mappedBy = "media", cascade = CascadeType.ALL)
    private List<Season<Anime>> seasons;
    
    @Getter
    public enum AnimeType {
        TV("TV动画"),
        MOVIE("剧场版"),
        OVA("OVA"),
        ONA("网络动画");
        
        private final String description;
        
        AnimeType(String description) {
            this.description = description;
        }

    }
} 