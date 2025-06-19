package com.movieflix.playbackservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "playbacks")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Playback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;

    private Long videoId;

    private Long timestamp; // seconds watched (user stopped at 325s)

    private LocalDateTime playedAt;
}

