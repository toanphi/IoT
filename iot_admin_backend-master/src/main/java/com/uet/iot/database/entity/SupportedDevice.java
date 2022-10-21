package com.uet.iot.database.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "supported_device")
public class SupportedDevice {

	/*
	 create table supported_device (
	 	id int auto_increment primary key,
	 	name varchar(255)
	 ) charset=utf8;
	 */

	public SupportedDevice(String name) {
		this.id = 0;
		this.name = name;
	}
	
	public SupportedDevice() {
		
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id = 0;
	
	@Column(name = "name")
	private String name;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
