package com.uet.iot.business;

import com.uet.iot.base.BaseResponse;
import org.springframework.http.ResponseEntity;

public interface ManageSupportedDeviceBusiness {
	/**
	 * fetch all supported device from original site
	 * @author tung
	 * @return
	 */
	public ResponseEntity<BaseResponse> fetchSupportedDevice();
	/**
	 * get all supported device from original site
	 * @author tung
	 * @return
	 */
	public ResponseEntity<BaseResponse> getAll();
}
