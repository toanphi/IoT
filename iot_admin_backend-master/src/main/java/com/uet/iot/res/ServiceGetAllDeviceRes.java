package com.uet.iot.res;

public class ServiceGetAllDeviceRes {
    private int deviceId;
    private String deviceName;
    private String productType;

    public int getDeviceId(){
        return this.deviceId;
    }

    public void setDeviceId(int deviceId){
        this.deviceId = deviceId;
    }

    public String getDeviceName(){
        return this.deviceName;
    }

    public void setDeviceName(String deviceName){
        this.deviceName = deviceName;
    }

    public String getProductType(){
        return this.productType;
    }

    public void setProductType(String productType){
        this.productType = productType;
    }
}
