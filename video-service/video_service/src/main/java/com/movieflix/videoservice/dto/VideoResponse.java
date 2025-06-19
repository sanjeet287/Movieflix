package com.movieflix.videoservice.dto;

import lombok.Data;

@Data
public class VideoResponse {
    private Long id;
    private String title;
    private String description;
    private String videoUrl;
    private String thumbnailUrl;
    private String resolution;
    private String format;
}