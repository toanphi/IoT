package com.uet.iot.database.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uet.iot.database.entity.SupportedDevice;

public interface SupportedDeviceRepo extends JpaRepository<SupportedDevice, Integer>, SupportedDeviceRepoCustom{

}
