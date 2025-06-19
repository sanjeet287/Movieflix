package com.movieflix.subscriptionservice.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.movieflix.subscriptionservice.dto.PlanDTO;
import com.movieflix.subscriptionservice.entity.DeviceType;
import com.movieflix.subscriptionservice.entity.UserDevice;
import com.movieflix.subscriptionservice.repository.UserDeviceRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDeviceServiceImpl implements UserDeviceService {

	private final UserDeviceRepository userDeviceRepository;
	private final UserSubscriptionService subscriptionService;
	private final PlanService planService;

	@Override
	public List<String> getActiveDeviceIds(String userId) {
		return userDeviceRepository.findByUserIdAndActiveTrue(userId).stream().map(UserDevice::getDeviceId)
				.collect(Collectors.toList());
	}

	@Override
	public boolean registerDevice(String userId, String deviceId, DeviceType deviceType) {
		log.info("Registering device {} for user {}", deviceId, userId);

		if (!subscriptionService.isUserSubscribed(userId)) {
			log.warn("User {} is not subscribed, device registration denied", userId);
			return false;
		}

		PlanDTO plan = planService.getPlanByName(subscriptionService.getUserPlanName(userId));

		// Check device type allowed
		if (!plan.getAllowedDeviceTypes().contains(deviceType)) {
			log.warn("Device type {} not allowed for plan {}", deviceType, plan.getName());
			return false;
		}

		List<UserDevice> activeDevices = userDeviceRepository.findByUserIdAndActiveTrue(userId);

		// Check if device already registered
		if (activeDevices.stream().anyMatch(d -> d.getDeviceId().equals(deviceId))) {

			userDeviceRepository.findByUserIdAndDeviceIdAndActiveTrue(userId, deviceId).ifPresent(d -> {
				d.setLastUsed(LocalDateTime.now());
				userDeviceRepository.save(d);
			});
			return true;
		}

		// Check max devices limit
		if (activeDevices.size() >= plan.getMaxDevices()) {
			log.warn("User {} reached max device limit for plan {}", userId, plan.getName());
			return false;
		}

		// Register new device
		UserDevice userDevice = UserDevice.builder().userId(userId).deviceId(deviceId).deviceType(deviceType)
				.lastUsed(LocalDateTime.now()).active(true).build();

		userDeviceRepository.save(userDevice);
		return true;
	}

	@Override
	public boolean isDeviceAllowed(String userId, String deviceId) {
		return userDeviceRepository.findByUserIdAndDeviceIdAndActiveTrue(userId, deviceId).isPresent();
	}

	@Override
	public void removeDevice(String userId, String deviceId) {
		userDeviceRepository.findByUserIdAndDeviceIdAndActiveTrue(userId, deviceId).ifPresent(device -> {
			device.setActive(false);
			userDeviceRepository.save(device);
			log.info("Device {} removed for user {}", deviceId, userId);
		});
	}
}
