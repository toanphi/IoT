package com.uet.iot.res;

import java.util.List;

public class DeviceControlData {
    private List<DeviceDataCount> deviceDataCountLst;
    private List<DeviceDataCountByHour> deviceDataCountByHourLst;

    public List<DeviceDataCount> getDeviceDataCountLst() {
        return deviceDataCountLst;
    }

    public void setDeviceDataCountLst(List<DeviceDataCount> deviceDataCountLst) {
        this.deviceDataCountLst = deviceDataCountLst;
    }

    public List<DeviceDataCountByHour> getDeviceDataCountByHourLst() {
        return deviceDataCountByHourLst;
    }

    public void setDeviceDataCountByHourLst(List<DeviceDataCountByHour> deviceDataCountByHourLst) {
        this.deviceDataCountByHourLst = deviceDataCountByHourLst;
    }
}
