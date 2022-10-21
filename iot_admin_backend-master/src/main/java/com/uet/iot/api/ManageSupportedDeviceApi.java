package com.uet.iot.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.uet.iot.base.BaseResponse;
import com.uet.iot.business.ManageSupportedDeviceBusiness;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Api(value = "Manage supported devices APIs")
public class ManageSupportedDeviceApi {
	
	@Autowired
	private ManageSupportedDeviceBusiness manageSupportedDeviceBusiness;
	
	@ApiOperation(value = "get all supported devices information api", response = ResponseEntity.class)
	@RequestMapping(value = "/device/supported/get/all", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getAllSupportedDevice() {
		return manageSupportedDeviceBusiness.getAll();
	}
	
	@ApiOperation(value = "fetch supported devices information api", response = ResponseEntity.class)
	@RequestMapping(value = "/device/supported/fetch", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> fetchSupportedDevice() {
		return manageSupportedDeviceBusiness.fetchSupportedDevice();
	}
}
