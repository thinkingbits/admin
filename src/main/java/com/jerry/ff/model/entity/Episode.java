package com.jerry.ff.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "episodes")
@Data
@NoArgsConstructor
public class Episode<T extends Media> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "season_id")
    private Season<T> season;

    private Integer episodeNumber;
    private String title;
    private String overview;
    private String stillUrl;
    private Integer duration;
    private LocalDate airDate;
} 