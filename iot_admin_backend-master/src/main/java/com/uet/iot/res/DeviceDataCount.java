package com.uet.iot.res;

public class DeviceDataCount {
    private String command;
    private int count = 1;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void count(){
        this.count++;
    }

    public DeviceDataCount(){};
    public DeviceDataCount(String command){
        this.command = command;
    }
}
