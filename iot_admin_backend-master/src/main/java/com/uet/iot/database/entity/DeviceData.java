package com.uet.iot.database.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "device_data")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceData {

    /*
    create table device_data (
      	id int primary key auto_increment,
      	data_type varchar(255),
      	data varchar(255),
        device_id int,
     	created_date datetime
     ) charset=utf8;
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "device_id")
    private int deviceId;
    @Column(name = "data")
    private String data;
    @Column(name = "data_type")
    private String dataType;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "created_date")
    private Date createdDate;

    public int getId() {
        return id;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public String getData() {
        return data;
    }

    public String getDataType() {
        return dataType;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}
