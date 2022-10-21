package com.uet.iot.business;

import com.uet.iot.base.BaseResponse;
import com.uet.iot.database.entity.Device;
import org.springframework.http.ResponseEntity;

public interface ManageDeviceBusiness {
	/**
	 * Get all devices information
	 * @author tung
	 * @return
	 */
	public ResponseEntity<BaseResponse> getAllDevice();
	/**
	 * insert or update device
	 * @author tung
	 * @param device
	 * @return
	 */
	public ResponseEntity<BaseResponse> saveDevice(Device device);
	/**
	 * remove device by id
	 * @author tung
	 * @param id
	 * @return
	 */
	public ResponseEntity<BaseResponse> removeDevice(int id);

	/**
	 * get list distinct product type 
	 * @author tung
	 * @return
	 */
	public ResponseEntity<BaseResponse> getDistinctProductType();
	
	/**
	 * get device by name
	 * @author tung
	 * @param name
	 * @return
	 */
	public ResponseEntity<BaseResponse> findByName(String name);
	
	/**
	 * get device by id
	 * @param id
	 * @return
	 */
	public ResponseEntity<BaseResponse> findById(int id);

	public ResponseEntity<BaseResponse> serviceGetAllDevice();
}
