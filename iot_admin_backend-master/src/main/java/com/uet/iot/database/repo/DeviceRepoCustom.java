package com.uet.iot.database.repo;

import java.util.List;

import com.uet.iot.database.entity.Device;

public interface DeviceRepoCustom {
	List<Device> findByName(String name);
	void deleteDevice(int id);
	List<String> findDistinctProductType();
	List<Device> findById(int id);
	List<Device> findByControlTopic(String controlTopic);
//	public void updateAutoIncrement();
}
