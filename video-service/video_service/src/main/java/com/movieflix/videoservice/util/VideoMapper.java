package com.movieflix.videoservice.util;

import java.time.LocalDateTime;

import com.movieflix.videoservice.dto.VideoRequest;
import com.movieflix.videoservice.dto.VideoResponse;
import com.movieflix.videoservice.entity.Video;

public class VideoMapper {

    public static Video toEntity(VideoRequest request) {
        Video video = new Video();
        video.setTitle(request.getTitle());
        video.setDescription(request.getDescription());
        video.setVideoUrl(request.getVideoUrl());
        video.setThumbnailUrl(request.getThumbnailUrl());
        video.setDuration(request.getDuration());
        video.setResolution(request.getResolution());
        video.setFormat(request.getFormat());
        video.setStatus("READY"); // default
        video.setUploadDate(LocalDateTime.now());
        return video;
    }

    public static VideoResponse toResponse(Video video) {
        VideoResponse response = new VideoResponse();
        response.setId(video.getId());
        response.setTitle(video.getTitle());
        response.setDescription(video.getDescription());
        response.setVideoUrl(video.getVideoUrl());
        response.setThumbnailUrl(video.getThumbnailUrl());
        response.setResolution(video.getResolution());
        response.setFormat(video.getFormat());
        return response;
    }
}