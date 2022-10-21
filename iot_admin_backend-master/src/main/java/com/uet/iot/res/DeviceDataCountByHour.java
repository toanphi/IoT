package com.uet.iot.res;

public class DeviceDataCountByHour {
    private String time;
    private int count = 0;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public DeviceDataCountByHour(){}

    public DeviceDataCountByHour(String time){
        this.time = time;
    }

    public void count(){
        this.count++;
    }
}
