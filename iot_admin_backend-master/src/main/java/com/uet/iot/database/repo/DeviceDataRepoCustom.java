package com.uet.iot.database.repo;

import com.uet.iot.res.DeviceDataCount;
import com.uet.iot.database.entity.DeviceData;

import java.util.Date;
import java.util.List;

public interface DeviceDataRepoCustom {

    List<DeviceData> getDeviceDataById(int deviceId, Date startDate, Date endDate);
    List<DeviceDataCount> getDeviceDataCountById(int deviceId);
    DeviceData getLastSwitchCommand(int deviceId);
}
