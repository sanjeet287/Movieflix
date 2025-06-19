package com.movieflix.videoservice.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.movieflix.videoservice.dto.VideoRequest;
import com.movieflix.videoservice.dto.VideoResponse;

public interface Videoservice  {
	
	VideoResponse saveVideo(VideoRequest request);
    List<VideoResponse> getAllVideos();
    VideoResponse getVideoById(Long id);
    void deleteVideo(Long id);
    VideoResponse uploadVideoWithFile(VideoRequest request, MultipartFile file, MultipartFile thumbnail) throws IOException;


}
