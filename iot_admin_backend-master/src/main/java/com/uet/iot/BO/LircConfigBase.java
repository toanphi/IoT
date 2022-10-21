package com.uet.iot.BO;

import java.util.HashMap;

public class LircConfigBase {
    private String deviceName;
    private HashMap<String, String> map = new HashMap<>();

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public HashMap<String, String> getMap() {
        return map;
    }

    public void setMap(HashMap<String, String> map) {
        this.map = map;
    }
}
