package com.uet.iot.api;

import org.jsoup.Connection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.uet.iot.base.BaseResponse;
import com.uet.iot.business.ManageDeviceBusiness;
import com.uet.iot.database.entity.Device;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Api(value = "Manage device APIs")
public class ManageDeviceApi {
	
	@Autowired
	private ManageDeviceBusiness manageDeviceBusiness;

	@ApiOperation(value = "get all devices information api", response = ResponseEntity.class)
	@RequestMapping(value = "/device", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getAllDevice() {
		return manageDeviceBusiness.getAllDevice();
	}
	
	@ApiOperation(value = "save device api", response = ResponseEntity.class)
	@RequestMapping(value = "/device", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> saveDevice(@RequestBody Device device) {
		System.out.println(device.getId() + " " + device.getName());
		return manageDeviceBusiness.saveDevice(device);
	}
	
	@ApiOperation(value = "get device by name api", response = ResponseEntity.class)
	@RequestMapping(value = "/device/{name}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> findByName(@PathVariable String name) {
		return manageDeviceBusiness.findByName(name);
	}
	
	@ApiOperation(value = "remove device api", response = ResponseEntity.class)
	@RequestMapping(value = "/device/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<BaseResponse> removeDevice(@PathVariable int id) {
		return manageDeviceBusiness.removeDevice(id);
	}
	
	@ApiOperation(value = "find device by id api", response = ResponseEntity.class)
	@RequestMapping(value = "/device/by/{id}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> findById(@PathVariable Integer id) {
		return manageDeviceBusiness.findById(id);
	}

	@ApiOperation(value = "get all devices information api for services", response = ResponseEntity.class)
	@RequestMapping(value = "/desvc", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> serviceGetAllDevice() {
		return manageDeviceBusiness.serviceGetAllDevice();
	}
}
