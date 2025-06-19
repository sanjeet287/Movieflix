package com.movieflix.videoservice.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.movieflix.videoservice.dto.VideoRequest;
import com.movieflix.videoservice.dto.VideoResponse;
import com.movieflix.videoservice.service.Videoservice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequestMapping("/api/videos")
@RestController
@RequiredArgsConstructor
@Slf4j
public class VideoController {

	private final Videoservice videoService;

	@GetMapping("/all")
	public ResponseEntity<List<VideoResponse>> getAllVideos() {
		return ResponseEntity.ok(videoService.getAllVideos());
	}

	@GetMapping("/{id}")
	public ResponseEntity<VideoResponse> getVideoById(@PathVariable Long id) {
		return ResponseEntity.ok(videoService.getVideoById(id));
	}

	@DeleteMapping("/remove/{id}")
	public ResponseEntity<String> deleteVideo(@PathVariable Long id) {
		videoService.deleteVideo(id);
		return ResponseEntity.ok("Video deleted successfully");
	}

	@PostMapping("/upload")
	public ResponseEntity<VideoResponse> uploadVideoWithFile(@RequestParam MultipartFile file,
			@RequestParam MultipartFile thumbnail, @RequestParam String title, @RequestParam String description,
			@RequestParam Long duration, @RequestParam String resolution, @RequestParam String format)
			throws IOException {

		log.info("inside upload file method: ");
		VideoRequest request = new VideoRequest();
		request.setTitle(title);
		request.setDescription(description);
		request.setDuration(duration);
		request.setResolution(resolution);
		request.setFormat(format);

		log.info("Content-Type: {}", ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
				.getRequest().getContentType());

		return ResponseEntity.ok(videoService.uploadVideoWithFile(request, file, thumbnail));
	}

}
