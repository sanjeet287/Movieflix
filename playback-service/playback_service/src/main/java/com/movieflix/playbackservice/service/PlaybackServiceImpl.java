package com.movieflix.playbackservice.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.movieflix.playbackservice.config.RabbitMQConfig;
import com.movieflix.playbackservice.dto.PlaybackRequestDTO;
import com.movieflix.playbackservice.dto.PlaybackResponseDTO;
import com.movieflix.playbackservice.entity.Playback;
import com.movieflix.playbackservice.repository.PlaybackRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlaybackServiceImpl implements PlaybackService {

    private final PlaybackRepository playbackRepository;
    private final RabbitTemplate rabbitTemplate;

    @Override
    public PlaybackResponseDTO logPlayback(PlaybackRequestDTO request) {
        log.info("Logging playback: userId={}, videoId={}, timestamp={}",
                 request.getUserId(), request.getVideoId(), request.getTimestamp());

        Playback playback = Playback.builder()
                .userId(request.getUserId())
                .videoId(request.getVideoId())
                .timestamp(request.getTimestamp())
                .playedAt(LocalDateTime.now())
                .build();

        Playback saved = playbackRepository.save(playback);
        log.debug("Playback saved with id: {}", saved.getId());

        // 🔁 Send event to RabbitMQ
        String message = String.format("User [%s] played video [%d] at [%s]",
                request.getUserId(), request.getVideoId(), playback.getPlayedAt());
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.ROUTING_KEY, message);
        log.info("Published playback message to RabbitMQ: {}", message);

        return mapToResponse(saved);
    }

    @Cacheable(value = "user_playbacks", key = "#userId")
    @Override
    public List<PlaybackResponseDTO> getPlaybackHistoryByUser(String userId) {
        log.info("Fetching playback history from DB for userId={}", userId);
        List<Playback> playbacks = playbackRepository.findByUserId(userId);
        log.debug("Found {} records for userId={}", playbacks.size(), userId);

        return playbacks.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<PlaybackResponseDTO> getPlaybackHistoryByVideo(Long videoId) {
        log.info("Fetching playback history for videoId={}", videoId);
        List<Playback> playbacks = playbackRepository.findByVideoId(videoId);
        log.debug("Found {} records for videoId={}", playbacks.size(), videoId);

        return playbacks.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private PlaybackResponseDTO mapToResponse(Playback playback) {
        return PlaybackResponseDTO.builder()
                .id(playback.getId())
                .userId(playback.getUserId())
                .videoId(playback.getVideoId())
                .timestamp(playback.getTimestamp())
                .playedAt(playback.getPlayedAt())
                .build();
    }

	
}
