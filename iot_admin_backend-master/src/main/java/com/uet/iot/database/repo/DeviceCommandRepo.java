package com.uet.iot.database.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uet.iot.database.entity.DeviceCommand;

public interface DeviceCommandRepo extends JpaRepository<DeviceCommand, String>, DeviceCommandRepoCustom{

}
