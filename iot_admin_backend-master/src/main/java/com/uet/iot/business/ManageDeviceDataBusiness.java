package com.uet.iot.business;

import com.uet.iot.base.BaseResponse;
import com.uet.iot.database.entity.DeviceData;
import org.springframework.http.ResponseEntity;

import java.util.Date;

public interface ManageDeviceDataBusiness {
	/**
	 * Get all devices data information
	 * @author tung
	 * @return BaseResponse
	 */
	ResponseEntity<BaseResponse> getAllDeviceData();

	ResponseEntity<BaseResponse> saveDeviceData(DeviceData data);

	ResponseEntity<BaseResponse> getDeviceDataById(int deviceId, Date startDate, Date endDate);

	ResponseEntity<BaseResponse> getDeviceDataCountById(int deviceId, Date startDate, Date endDate);

	ResponseEntity<BaseResponse> getLastSwitchData(String controlTopic);
}
