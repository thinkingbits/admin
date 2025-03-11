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

    private String title;

    private String originalTitle;

    private Double rating;

    private String region;

    private String language;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Integer duration;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    private String posterUrl;

    private String bannerUrl;

    private String videoUrl;

    private LocalDate releaseDate;

    private LocalDate endDate;

    private Integer seriesOrder;  // 在系列中的顺序，比如第1部、第2部等

    @Column(nullable = false)
    @Builder.Default
    private Integer status = 1; // 0-下架, 1-上架

    @ManyToMany
    @JoinTable(
            name = "actors",
            joinColumns = @JoinColumn(name = "film_id"),
            inverseJoinColumns = @JoinColumn(name = "actor_id")
    )
    private List<Actor> actors;

    @ManyToMany
    @JoinTable(
            name = "directors",
            joinColumns = @JoinColumn(name = "film_id"),
            inverseJoinColumns = @JoinColumn(name = "director_id")
    )
    private List<Director> directors;

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