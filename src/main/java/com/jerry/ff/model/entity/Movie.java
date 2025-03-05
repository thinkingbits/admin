package com.jerry.ff.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "movies")
@Builder
@NoArgsConstructor
@Data
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Movie extends Media {

    private Integer duration;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    private Integer seriesOrder;  // 在系列中的顺序，比如第1部、第2部等

    @Column(nullable = false)
    @Builder.Default
    private Integer status = 1; // 0-下架, 1-上架
} 