package com.movieflix.videoservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "videos")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    
    private String description;

    private String videoUrl;  // URL where the video file is stored (S3 or Firebase)
    
    private String thumbnailUrl;

    private Long duration;  // duration in seconds

    private String resolution; //  1080p, 720p

    private String format; //  mp4, mkv

    private LocalDateTime uploadDate;

    // Status like PROCESSING, READY, FAILED if transcoding applies
    private String status;

    
}
