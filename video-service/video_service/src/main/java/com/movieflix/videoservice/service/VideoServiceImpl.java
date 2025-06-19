package com.movieflix.videoservice.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.movieflix.videoservice.config.RabbitMQConfig;
import com.movieflix.videoservice.dto.VideoRequest;
import com.movieflix.videoservice.dto.VideoResponse;
import com.movieflix.videoservice.entity.Video;
import com.movieflix.videoservice.repository.VideoRepository;
import com.movieflix.videoservice.util.VideoMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class VideoServiceImpl implements Videoservice {
	
	private final VideoRepository videoRepository;
	
	 private final RabbitTemplate rabbitTemplate;


	 @Override
	    public VideoResponse saveVideo(VideoRequest request) {
	        Video video = VideoMapper.toEntity(request);
	        Video saved = videoRepository.save(video);

	        // Send to RabbitMQ queue
	        rabbitTemplate.convertAndSend(RabbitMQConfig.VIDEO_TRANSCODE_QUEUE, saved.getId());

	        return VideoMapper.toResponse(saved);
	    }

	    @Override
	    public List<VideoResponse> getAllVideos() {
	        return videoRepository.findAll()
	                .stream()
	                .map(VideoMapper::toResponse)
	                .collect(Collectors.toList());
	    }

	    @Override
	    public VideoResponse getVideoById(Long id) {
	        Video video = videoRepository.findById(id)
	                .orElseThrow(() -> new NoSuchElementException("Video not found with ID: " + id));

	        return VideoMapper.toResponse(video);
	    }

	    @Override
	    public void deleteVideo(Long id) {
	        if (!videoRepository.existsById(id)) {
	            throw new NoSuchElementException("Video not found with ID: " + id);
	        }
	        videoRepository.deleteById(id);
	    }

	    @Override
	    public VideoResponse uploadVideoWithFile(VideoRequest request, MultipartFile file, MultipartFile thumbnail) throws IOException {
	        log.info("Starting video upload for title: {}", request.getTitle());

	        String videoPath = saveFile(file, "videos");
	        log.info("Video file saved at: {}", videoPath);

	        String thumbnailPath = saveFile(thumbnail, "thumbnails");
	        log.info("Thumbnail file saved at: {}", thumbnailPath);

	        request.setVideoUrl(videoPath);
	        request.setThumbnailUrl(thumbnailPath);

	        Video saved = videoRepository.save(VideoMapper.toEntity(request));
	        log.info("Video metadata saved with ID: {}", saved.getId());

	        rabbitTemplate.convertAndSend(RabbitMQConfig.VIDEO_TRANSCODE_QUEUE, saved.getId());
	        log.info("Video ID {} published to transcoding queue", saved.getId());

	        return VideoMapper.toResponse(saved);
	    }

	    
	    private String saveFile(MultipartFile file, String folder) throws IOException {
	        String uploadDir = "uploads/" + folder;
	        File directory = new File(uploadDir);
	        if (!directory.exists()) {
	            boolean created = directory.mkdirs();
	            log.info("Created directory '{}': {}", uploadDir, created);
	        }

	        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
	        Path filePath = Paths.get(uploadDir, filename);
	        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

	        log.info("Saved file: {}", filePath.toString());
	        return filePath.toString();
	    }


}
