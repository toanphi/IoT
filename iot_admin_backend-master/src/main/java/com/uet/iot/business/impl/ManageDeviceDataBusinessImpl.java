package com.uet.iot.business.impl;

import com.uet.iot.database.entity.Device;
import com.uet.iot.res.DeviceControlData;
import com.uet.iot.res.DeviceDataCount;
import com.uet.iot.res.DeviceDataCountByHour;
import com.uet.iot.base.BaseResponse;
import com.uet.iot.business.ManageDeviceDataBusiness;
import com.uet.iot.database.entity.DeviceData;
import com.uet.iot.database.repo.DeviceDataRepo;
import com.uet.iot.database.repo.DeviceRepo;
import com.uet.iot.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;

@Service
public class ManageDeviceDataBusinessImpl implements ManageDeviceDataBusiness{

	@Autowired
	private DeviceDataRepo deviceDataRepo;

	@Autowired
	private DeviceRepo deviceRepo;

	@Override
	public ResponseEntity<BaseResponse> getAllDeviceData() {
		return new ResponseEntity<>(new BaseResponse(deviceDataRepo.findAll()), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<BaseResponse> saveDeviceData(DeviceData data) {
		if (Util.isNull(data)){
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		if (Util.isNullOrEmpty(data.getData()) || Util.isNullOrEmpty(data.getDeviceId())){
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(new BaseResponse(deviceDataRepo.save(data)), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<BaseResponse> getDeviceDataById(int deviceId, Date startDate, Date endDate) {
		if (Util.isNullOrEmpty(deviceId)){
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		List<DeviceData> lst = deviceDataRepo.getDeviceDataById(deviceId, startDate, endDate);
//
//		for (DeviceData deviceData : lst) {
//			boolean b = !(deviceData.getCreatedDate().after(endDate) && deviceData.getCreatedDate().before(endDate));
//			if (b) {
//				lst.remove(deviceData);
//			}
//		}

		return new ResponseEntity<>(new BaseResponse(lst), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<BaseResponse> getDeviceDataCountById(int deviceId, Date startDate, Date endDate) {

		if (Util.isNullOrEmpty(deviceId)){
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		Map<String, DeviceDataCount> deviceDataCountMap = new HashMap<>();

		List<DeviceDataCountByHour> deviceDataCountByHourLst = new ArrayList<>();

		DeviceDataCountByHour bedtime = new DeviceDataCountByHour("Buổi đêm");
		DeviceDataCountByHour morning = new DeviceDataCountByHour("Buổi sáng");
		DeviceDataCountByHour noon = new DeviceDataCountByHour("Buổi trưa");
		DeviceDataCountByHour afternoon = new DeviceDataCountByHour("Buổi chiều");
		DeviceDataCountByHour evening = new DeviceDataCountByHour("Buổi tối");

		List<DeviceData> deviceData = deviceDataRepo.getDeviceDataById(deviceId, startDate, endDate);

		deviceData.forEach(d -> {
			if (deviceDataCountMap.containsKey(d.getData())){
				deviceDataCountMap.get(d.getData()).count();
			} else {
				deviceDataCountMap.put(d.getData(), new DeviceDataCount(d.getData()));
			}

//			ZoneId.getAvailableZoneIds().forEach(s -> {
//				System.out.println(s);
//			});

			LocalTime dataTime = LocalDateTime.ofInstant(d.getCreatedDate().toInstant(), ZoneId.of("UCT")).toLocalTime();
			if (isBedTime(dataTime)){
				bedtime.count();
			} else if (isMorning(dataTime)){
				morning.count();
			} else if (isNoon(dataTime)){
				noon.count();
			} else if (isAfterNoon(dataTime)){
				afternoon.count();
			} else if (isNight(dataTime)){
				evening.count();
			}
		});

		deviceDataCountByHourLst.add(bedtime);
		deviceDataCountByHourLst.add(morning);
		deviceDataCountByHourLst.add(noon);
		deviceDataCountByHourLst.add(afternoon);
		deviceDataCountByHourLst.add(evening);

		DeviceControlData deviceControlData = new DeviceControlData();
		deviceControlData.setDeviceDataCountLst(new ArrayList<>(deviceDataCountMap.values()));
		deviceControlData.setDeviceDataCountByHourLst(deviceDataCountByHourLst);

		return new ResponseEntity<>(new BaseResponse(deviceControlData), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<BaseResponse> getLastSwitchData(String controlTopic) {
		List<Device> lstSwitch = deviceRepo.findByControlTopic(controlTopic);
		List<String> lstLastCommand = new ArrayList<>();

		Map<String, Integer> map = new HashMap<>();

		for (int i = 0 ; i < lstSwitch.size() ; i++){
			Device s = lstSwitch.get(i);
			DeviceData data = deviceDataRepo.getLastSwitchCommand(s.getId());;

			int value = 0;
			if (data.getData() != null && data.getData().equals("KEY_ON")){
				value = 100;
			}
			String out = "out" + s.getGangs();
			map.put(out, value);
		}
		return new ResponseEntity(new BaseResponse(map), HttpStatus.OK);
	}

	public boolean isBedTime(LocalTime time) {
		return (time.isAfter(LocalTime.of(22, 0)) && time.isBefore(LocalTime.of(23, 0)))
				|| (time.isAfter(LocalTime.of(1, 0)) && time.isBefore(LocalTime.of(6, 0)));
	}

	public boolean isMorning(LocalTime time) {
		return time.isAfter(LocalTime.of(6, 0)) && time.isBefore(LocalTime.of(11, 0));
	}

	public boolean isNoon(LocalTime time) {
		return time.isAfter(LocalTime.of(11, 0)) && time.isBefore(LocalTime.of(13, 0));
	}

	public boolean isAfterNoon(LocalTime time){
		return time.isAfter(LocalTime.of(13, 0)) && time.isBefore(LocalTime.of(18, 0));
	}

	public boolean isNight(LocalTime time){
		return time.isAfter(LocalTime.of(18, 0)) && time.isBefore(LocalTime.of(22, 0));
	}
}
