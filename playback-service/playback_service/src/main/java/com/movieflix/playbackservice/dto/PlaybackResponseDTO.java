package com.movieflix.playbackservice.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaybackResponseDTO {

	private Long id;
	private String userId;
	private Long videoId;
	private Long timestamp;
	private LocalDateTime playedAt;
}
