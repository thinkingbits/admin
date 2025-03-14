package com.jerry.ff.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "films")
@Builder
@NoArgsConstructor
@Data
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Film {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer type;

    // 译名
    private String title;

    // 原始名
    private String originalTitle;

    // 评分
    private Double rating;

    // 地区
    private String region;

    private String language;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    private String posterUrl;

    private String bannerUrl;

    private String videoUrl;

    // 发行时间
    private LocalDate airDate;

    @Column(nullable = false)
    @Builder.Default
    private Integer status = 1; // 0-下架, 1-上架

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "film_actors",
            joinColumns = @JoinColumn(name = "film_id"),
            inverseJoinColumns = @JoinColumn(name = "actor_id")
    )
    private List<Actor> actors;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "film_directors",
            joinColumns = @JoinColumn(name = "film_id"),
            inverseJoinColumns = @JoinColumn(name = "director_id")
    )
    private List<Director> directors;

    @Column(nullable = false)
    private LocalDateTime createAt;

    @Column(nullable = false)
    private LocalDateTime updateAt;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        createAt = now;
        updateAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        updateAt = LocalDateTime.now();
    }
} 