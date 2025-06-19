package com.movieflix.playbackservice.service;

import java.util.List;

import com.movieflix.playbackservice.dto.PlaybackRequestDTO;
import com.movieflix.playbackservice.dto.PlaybackResponseDTO;

public interface PlaybackService {

	PlaybackResponseDTO logPlayback(PlaybackRequestDTO request);

	List<PlaybackResponseDTO> getPlaybackHistoryByUser(String userId); 

	List<PlaybackResponseDTO> getPlaybackHistoryByVideo(Long videoId);
}
