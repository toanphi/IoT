package com.uet.iot.database.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "device")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Device {
	
	/*
	create table device(
		id int primary key auto_increment,
		name varchar(255),
		model varchar(255),
		product_type varchar(255), 
		control_topic varchar(1000), 
		output_topic varchar(1000),
		gangs int, 
		created_date datetime, 
		description varchar(1000), 
		status bit, 
		constraint device_unique unique(name)
	) charset=utf8;
*/
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	@Column(unique = true, name = "name")
	private String name;
	@Column(name = "model")
	private String model;
	@Column(name = "product_type")
	private String productType;
	@Column(name = "control_topic")
	private String controlTopic;
	@Column(name = "output_topic")
	private String outputTopic;
	@Column(name = "gangs")
	private int gangs;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "created_date")
	private Date createdDate;
	@Column(name = "description")
	private String description;
	@Column(name = "status")
	private boolean status;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public int getGangs() {
		return gangs;
	}
	public void setGangs(int gangs) {
		this.gangs = gangs;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	public String getControlTopic() {
		return controlTopic;
	}
	public void setControlTopic(String controlTopic) {
		this.controlTopic = controlTopic;
	}
	public String getOutputTopic() {
		return outputTopic;
	}
	public void setOutputTopic(String outputTopic) {
		this.outputTopic = outputTopic;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

}
