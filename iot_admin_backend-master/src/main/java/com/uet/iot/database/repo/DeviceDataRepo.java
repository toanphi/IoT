package com.uet.iot.database.repo;

import com.uet.iot.database.entity.DeviceData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DeviceDataRepo extends JpaRepository<DeviceData, Integer>, DeviceDataRepoCustom {
}
