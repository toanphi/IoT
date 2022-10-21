package com.uet.iot.database.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uet.iot.database.entity.Device;

@Repository
public interface DeviceRepo extends JpaRepository<Device, Integer>, DeviceRepoCustom{

}
