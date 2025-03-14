package com.jerry.ff.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "directors")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Director {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String originalName;

    private String biography;

    private String photoUrl;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "directors")
    private List<Film> films;

    @CreatedDate
    private LocalDateTime createAt;

    @LastModifiedDate  
    private LocalDateTime updateAt;
} 