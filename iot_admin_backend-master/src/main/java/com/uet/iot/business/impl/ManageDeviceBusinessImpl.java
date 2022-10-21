package com.uet.iot.business.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.uet.iot.base.BaseResponse;
import com.uet.iot.business.ManageDeviceBusiness;
import com.uet.iot.database.entity.Device;
import com.uet.iot.database.repo.DeviceRepo;
import com.uet.iot.util.Message;
import com.uet.iot.util.Util;
import com.uet.iot.res.ServiceGetAllDeviceRes;

@Service
public class ManageDeviceBusinessImpl implements ManageDeviceBusiness{
	
	@Autowired
	private DeviceRepo deviceRepo;
	
	@Override
	public ResponseEntity<BaseResponse> getAllDevice() {
		return new ResponseEntity<>( new BaseResponse(deviceRepo.findAll()), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<BaseResponse> saveDevice(Device device){
		BaseResponse response = new BaseResponse();
		if (Util.isNull(device)) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		try {
			response.setData(deviceRepo.saveAndFlush(device));
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (DataIntegrityViolationException e) {
//			deviceRepo.updateAutoIncrement();
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}
		
	}

	@Override
	public ResponseEntity<BaseResponse> removeDevice(int id) throws DataIntegrityViolationException {
		BaseResponse response = new BaseResponse();
		if (Util.isNullOrEmpty(id)) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		deviceRepo.deleteDevice(id);
//		deviceRepo.updateAutoIncrement();
		response.setData(Message.DELETED);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<BaseResponse> getDistinctProductType() {
		BaseResponse response = new BaseResponse();
		response.setData(deviceRepo.findDistinctProductType());
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<BaseResponse> findByName(String name) {
		BaseResponse response = new BaseResponse();
		if (Util.isNullOrEmpty(name)) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		List<Device> lst = deviceRepo.findByName(name);
		if (Util.isNullOrEmpty(lst)) {
			response.setData(new ArrayList<>());
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}

		response.setData(lst.get(0));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<BaseResponse> findById(int id) {
		BaseResponse response = new BaseResponse();
		if (Util.isNullOrEmpty(id)) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		List<Device> lst = deviceRepo.findById(id);
		
		if (Util.isNullOrEmpty(lst)) {
			response.setData(new ArrayList<>());
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}

		response.setData(lst.get(0));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<BaseResponse> serviceGetAllDevice(){
		List<Device> lst = deviceRepo.findAll();

		List<ServiceGetAllDeviceRes> lstRes = new ArrayList<>();

		lst.forEach(device -> {
			ServiceGetAllDeviceRes res = new ServiceGetAllDeviceRes();
			res.setDeviceId(device.getId());
			res.setDeviceName(device.getName());
			res.setProductType(device.getProductType());
			lstRes.add(res);
		});

		return new ResponseEntity<>(new BaseResponse(lstRes), HttpStatus.OK);
	}
}
