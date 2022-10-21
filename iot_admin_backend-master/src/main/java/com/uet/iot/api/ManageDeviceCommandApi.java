package com.uet.iot.api;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.uet.iot.base.BaseResponse;
import com.uet.iot.business.ManageDeviceCommandBusiness;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Api(value = "Manage device command APIs")
public class ManageDeviceCommandApi {
	
	@Autowired
	private ManageDeviceCommandBusiness manageDeviceCommandBusiness;
	
	@ApiOperation(value = "get all devices command information api", response = ResponseEntity.class)
	@RequestMapping(value = "/device/command", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getAllDeviceCommand() {
		return manageDeviceCommandBusiness.getAll();
	}
	
	@ApiOperation(value = "reset all devices command to default api", response = ResponseEntity.class)
	@RequestMapping(value = "/device/command/reset", method = RequestMethod.PUT)
	public ResponseEntity<BaseResponse> resetToDefaultSupportedCommand() {
		return manageDeviceCommandBusiness.resetToDefaultSupportedCommand();
	}
	
	@ApiOperation(value = "get exact types by type", response = ResponseEntity.class)
	@RequestMapping(value = "/device/command/{type}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> findByType(@PathVariable String type) {
		return manageDeviceCommandBusiness.findByType(type);
	}

	@ApiOperation(value = "service get exact types by type", response = ResponseEntity.class)
	@RequestMapping(value = "/desvc/command/{type}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> serviceFindByType(@PathVariable String type) {
		return manageDeviceCommandBusiness.findByType(type);
	}

	@ApiOperation(value = "service get all devices command information api", response = ResponseEntity.class)
	@RequestMapping(value = "/desvc/command", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> serviceGetAllDeviceCommand() {
		return manageDeviceCommandBusiness.getAll();
	}
	
	@ApiOperation(value = "get exact command by type and phrase", response = ResponseEntity.class)
	@RequestMapping(value = "/device/command/{type}/{phrase}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> findExact(@PathVariable String type, @PathVariable String phrase) {
		return manageDeviceCommandBusiness.findExact(type, phrase);
	}

	@ApiOperation(value = "upload file", response = ResponseEntity.class)
	@RequestMapping(value = "/device/command/upload", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> upload(@RequestParam("file") MultipartFile file) throws IOException, InvalidFormatException {
		return manageDeviceCommandBusiness.upload(file);
	}
}
