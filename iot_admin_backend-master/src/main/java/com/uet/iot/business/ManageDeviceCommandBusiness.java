package com.uet.iot.business;

import com.uet.iot.base.BaseResponse;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ManageDeviceCommandBusiness {
	/**
	 * get all command data
	 * @author tung
	 * @return BaseResponse
	 */
	public ResponseEntity<BaseResponse> getAll();

	/**
	 * reset list supported commands 
	 * @author tung
	 * @return BaseResponse
	 */
	public ResponseEntity<BaseResponse> resetToDefaultSupportedCommand();
	/**
	 * Find commands by type
	 * @author tung
	 * @param type type of command or device
	 * @return BaseResponse
	 */
	public ResponseEntity<BaseResponse> findByType(String type);
	
	public ResponseEntity<BaseResponse> findExact(String type, String phrase);

	public ResponseEntity<BaseResponse> createLircConfig(String command);

	ResponseEntity<BaseResponse> upload(MultipartFile file) throws IOException, InvalidFormatException;
}
