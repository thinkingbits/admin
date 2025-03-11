package com.jerry.ff.model.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "animes")
@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Anime extends Media {

    @Column(nullable = false)
    @Builder.Default
    private Integer status = 1; // 0-下架, 1-上架

    private String studio;  // 制作公司
    private String season;  // 播出季度，如：2024年春季
    private String broadcast;  // 播出时间，如：每周日 23:00

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
    
    @OneToMany(mappedBy = "mediaId", cascade = CascadeType.ALL)
    private List<Season> seasons;

    @Enumerated(EnumType.STRING)
    private AnimeType type;  // TV, MOVIE, OVA, ONA

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