package com.movieflix.subscriptionservice.service;

import com.movieflix.subscriptionservice.entity.DeviceType;

import java.util.List;

public interface UserDeviceService {

    List<String> getActiveDeviceIds(String userId);

    boolean registerDevice(String userId, String deviceId, DeviceType deviceType);

    boolean isDeviceAllowed(String userId, String deviceId);

    void removeDevice(String userId, String deviceId);
}
