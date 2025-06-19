package com.movieflix.subscriptionservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.movieflix.subscriptionservice.entity.UserDevice;

public interface UserDeviceRepository extends JpaRepository<UserDevice, Long> {

	List<UserDevice> findByUserIdAndActiveTrue(String userId);

	Optional<UserDevice> findByUserIdAndDeviceIdAndActiveTrue(String userId, String deviceId);
}
