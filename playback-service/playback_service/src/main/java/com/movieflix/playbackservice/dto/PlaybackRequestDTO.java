package com.movieflix.playbackservice.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaybackRequestDTO {
	
	private String userId;
	private Long videoId;
	private Long timestamp;
}