package com.movieflix.videoservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.movieflix.videoservice.entity.Video;

public interface VideoRepository extends JpaRepository<Video, Long>{

}
