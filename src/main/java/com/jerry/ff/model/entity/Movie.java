package com.jerry.ff.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "movies")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Movie extends Media {

    private Integer duration;

    @Builder.Default
    private Double rating = 0.0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_series_id")
    private MovieSeries movieSeries;

    private Integer seriesOrder;  // 在系列中的顺序，比如第1部、第2部等

    @ManyToMany
    @JoinTable(
        name = "directors",
        joinColumns = @JoinColumn(name = "movie_id"),
        inverseJoinColumns = @JoinColumn(name = "director_id")
    )
    @Builder.Default
    private Set<Director> directors = new HashSet<>();

    @Column(nullable = false)
    @Builder.Default
    private Integer status = 1; // 0-下架, 1-上架

    @CreatedDate
    private LocalDateTime createTime;

    @LastModifiedDate
    private LocalDateTime updateTime;
} 