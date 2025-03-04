package com.jerry.ff.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "seasons")
@Data
@NoArgsConstructor
public class Season<T extends Media> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "media_id")
    private T media;

    private Integer seasonNumber;
    private String title;
    private String overview;
    private String posterUrl;

    @OneToMany(mappedBy = "season", cascade = CascadeType.ALL)
    private List<Episode> episodes;
} 