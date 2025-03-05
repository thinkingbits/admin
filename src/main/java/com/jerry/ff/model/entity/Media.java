package com.jerry.ff.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@MappedSuperclass
@Data
@NoArgsConstructor
public abstract class Media {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String originalTitle;

    private Double rating;
    private String region;
    private String language;
    @Column(columnDefinition = "TEXT")
    private String description;

//    @ManyToMany
//    @JoinTable(
//            name = "actors",
//            joinColumns = @JoinColumn(name = "media_id"),
//            inverseJoinColumns = @JoinColumn(name = "actor_id")
//    )
//    private List<Actor> actors;
//
//    @ManyToMany
//    @JoinTable(
//            name = "directors",
//            joinColumns = @JoinColumn(name = "media_id"),
//            inverseJoinColumns = @JoinColumn(name = "director_id")
//    )
//    private List<Director> directors;
    
    private String posterUrl;
    private String bannerUrl;
    private String videoUrl;

    private LocalDate releaseDate;
    private LocalDate endDate;
    
    @Column(nullable = false)
    private LocalDateTime createTime;
    
    @Column(nullable = false)
    private LocalDateTime updateTime;
    
    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        createTime = now;
        updateTime = now;
    }
    
    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
} 