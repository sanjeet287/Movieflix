package com.movieflix.playbackservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.movieflix.playbackservice.entity.Playback;

public interface PlaybackRepository extends JpaRepository<Playback, Long> {

	List<Playback> findByUserId(String userId); // changed from Long

	List<Playback> findByVideoId(Long videoId);
}
