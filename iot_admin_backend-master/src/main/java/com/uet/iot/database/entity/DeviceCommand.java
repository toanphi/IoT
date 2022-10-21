package com.uet.iot.database.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "device_command")
public class DeviceCommand {
	
	/*
	  create table device_command (
	  		id int primary key,
	  		type varchar(255),
	 		command varchar(255),
	 		phrase_command varchar(1000)
	  ) charset=utf8;
	 */
	
	@Id
	@Column(name = "id")
	private int id;
	@Column(name = "type")
	private String type;
	@Column(name = "command")
	private String command;
	@Column(name = "phrase_command")
	private String phraseCommand;
	
	public DeviceCommand() {}
	
	public DeviceCommand(int id, String type, String command, String phraseCommand) {
		this.id = id;
		this.type = type;
		this.command = command;
		this.phraseCommand = phraseCommand;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
	}
	public String getPhraseCommand() {
		return phraseCommand;
	}
	public void setPhraseCommand(String phraseCommand) {
		this.phraseCommand = phraseCommand;
	}
}
