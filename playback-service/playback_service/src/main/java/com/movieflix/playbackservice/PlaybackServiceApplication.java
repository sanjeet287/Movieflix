package com.movieflix.playbackservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.movieflix.playbackservice"})
@EnableCaching
public class PlaybackServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlaybackServiceApplication.class, args);
	}

}
