package com.movieflix.playbackservice.controller;

import com.movieflix.playbackservice.dto.PlaybackRequestDTO;
import com.movieflix.playbackservice.dto.PlaybackResponseDTO;
import com.movieflix.playbackservice.service.PlaybackService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/playbacks")
@RequiredArgsConstructor
@Slf4j
public class PlaybackController {

    private final PlaybackService playbackService;

    @PostMapping("/playback")
    public ResponseEntity<PlaybackResponseDTO> logPlayback(@RequestBody PlaybackRequestDTO request) {
        log.info("Received request to log playback: {}", request);
        PlaybackResponseDTO response = playbackService.logPlayback(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PlaybackResponseDTO>> getPlaybackByUser(@PathVariable String userId) {
        log.info("Fetching playback history for userId={}", userId);
        List<PlaybackResponseDTO> responses = playbackService.getPlaybackHistoryByUser(userId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/video/{videoId}")
    public ResponseEntity<List<PlaybackResponseDTO>> getPlaybackByVideo(@PathVariable Long videoId) {
        log.info("Fetching playback history for videoId={}", videoId);
        List<PlaybackResponseDTO> responses = playbackService.getPlaybackHistoryByVideo(videoId);
        return ResponseEntity.ok(responses);
    }
}
