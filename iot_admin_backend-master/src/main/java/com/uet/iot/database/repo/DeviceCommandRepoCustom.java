package com.uet.iot.database.repo;

import java.util.List;

import com.uet.iot.database.entity.DeviceCommand;

public interface DeviceCommandRepoCustom {
	public List<DeviceCommand> findByType(String type);
	
	public List<DeviceCommand> findExact(String type, String phrase);
}	
