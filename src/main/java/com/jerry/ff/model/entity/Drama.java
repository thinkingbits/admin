package com.jerry.ff.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "dramas")
@Data
@NoArgsConstructor
public class Drama extends Media {
    private Integer status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "media", cascade = CascadeType.ALL)
    private List<Season<Drama>> seasons;
} 