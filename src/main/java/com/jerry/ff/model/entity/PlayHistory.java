package com.jerry.ff.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "play_histories")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class PlayHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "episode_id", nullable = false)
    private Episode episode;

    @Column(name = "play_position")
    private Integer playPosition = 0;  // 播放位置（秒）

    @Column(name = "duration")
    private Integer duration = 0;      // 视频总时长（秒）

    @Column(name = "watch_status")
    private Integer watchStatus = 0;   // 观看状态：0-未完成 1-已完成

    @Column(name = "last_play_time")
    private LocalDateTime lastPlayTime;  // 最后播放时间

    @CreatedDate
    @Column(name = "create_time", updatable = false)
    private LocalDateTime createAt;    // 首次播放时间

    @LastModifiedDate
    @Column(name = "update_time")
    private LocalDateTime updateAt;    // 更新时间
} 