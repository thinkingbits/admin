package com.jerry.ff.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "episodes")
@Data
@NoArgsConstructor
public class Episode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "season_id")
    private Season season;

    @OneToMany(mappedBy = "episode", fetch = FetchType.LAZY)
    private List<Source> sources;

    @OneToMany(mappedBy = "episode", fetch = FetchType.LAZY)
    private List<Comment> comments;

    private Integer episodeNumber;

    private String title;

    private String overview;

    private String stillUrl;

    private Integer duration;

    private Integer viewCount;

    private LocalDate airDate;
} 