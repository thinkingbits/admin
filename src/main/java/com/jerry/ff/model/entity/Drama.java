package com.jerry.ff.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "dramas")
@Data
@NoArgsConstructor
public class Drama extends Media {
    private Integer totalEpisodes;
    private Integer currentEpisodes;
    private Double rating;
    private Long ratingCount;
    private Long categoryId;
    private String categoryName;
    private String region;
    private String language;
    private Integer status;

    @OneToMany(mappedBy = "media", cascade = CascadeType.ALL)
    private List<Season<Drama>> seasons;

    @ManyToMany
    @JoinTable(
        name = "actors",
        joinColumns = @JoinColumn(name = "drama_id"),
        inverseJoinColumns = @JoinColumn(name = "actor_id")
    )
    private List<Actor> actors;
} 