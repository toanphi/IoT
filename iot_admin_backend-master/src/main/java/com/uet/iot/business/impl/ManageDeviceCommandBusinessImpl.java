package com.uet.iot.business.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.uet.iot.base.BaseResponse;
import com.uet.iot.business.ManageDeviceCommandBusiness;
import com.uet.iot.database.entity.DeviceCommand;
import com.uet.iot.database.repo.DeviceCommandRepo;
import com.uet.iot.util.Message;
import com.uet.iot.util.Util;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ManageDeviceCommandBusinessImpl implements ManageDeviceCommandBusiness {

	private final int COLUMN_INDEX_PRODUCT_TYPE = 0;
	private final int COLUMN_INDEX_COMMAND = 1;
	private final int COLUMN_INDEX_PHRASE_COMMAND = 2;

	private final String[] DEFAULT_COMMAND = {
		// television
		"tivi,KEY_VOLUMEUP,tăng âm lượng", // volume up,
		"tivi,KEY_VOLUMEDOWN,giảm âm lượng", // volume down
		"tivi,KEY_CHANNELUP,tiến kênh:tăng kênh", // channel up
		"tivi,KEY_CHANNELDOWN,lùi kênh", // channel down
		"tivi,KEY_0,kênh 0:số 0:key0",
		"tivi,KEY_1,kênh 1:số 1:key1",
		"tivi,KEY_2,kênh 2:số 2:key2",
		"tivi,KEY_3,kênh 3:số 3:key3",
		"tivi,KEY_4,kênh 4:số 4:key4",
		"tivi,KEY_5,kênh 5:số 5:key5",
		"tivi,KEY_6,kênh 6:số 6:key6",
		"tivi,KEY_7,kênh 7:số 7:key7",
		"tivi,KEY_8,kênh 8:số 8:key8",
		"tivi,KEY_9,kênh 9:số 9:key9",
		"tivi,KEY_POWER,bật:tắt", // on off

		// switch
		"switch,KEY_ON,bật",
		"switch,KEY_OFF,tắt",

		// fan
		"fan,KEY_SPDLOW,số 1",
		"fan,KEY_SPDMED,số 2",
		"fan,KEY_SPDHIGH,số 3",
		"fan,KEY_POWER,bật:tắt",
		
		// air conditioner
		"air-conditioner,KEY_ON,bật",
		"air-conditioner,KEY_OFF,tắt",
		"air-conditioner,KEY_AIRLOW,gió nhẹ",
		"air-conditioner,KEY_AIRMED,gió vừa",
		"air-conditioner,KEY_AIRHIGH,gió lớn",
		"air-conditioner,KEY_SWING,quay",
		"air-conditioner,KEY_18,18 độ",
		"air-conditioner,KEY_19,19 độ",
		"air-conditioner,KEY_20,20 độ",
		"air-conditioner,KEY_21,21 độ",
		"air-conditioner,KEY_22,22 độ",
		"air-conditioner,KEY_23,23 độ",
		"air-conditioner,KEY_24,24 độ",
		"air-conditioner,KEY_25,25 độ",
		"air-conditioner,KEY_26,26 độ",
		"air-conditioner,KEY_27,27 độ",
		"air-conditioner,KEY_28,28 độ",
		"air-conditioner,KEY_29,29 độ",
		"air-conditioner,KEY_30,30 độ",
	};

	@Autowired
	private DeviceCommandRepo deviceCommandRepo;

	@Override
	public ResponseEntity<BaseResponse> getAll() {
		return new ResponseEntity<>(new BaseResponse(deviceCommandRepo.findAll()), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<BaseResponse> resetToDefaultSupportedCommand() {
		BaseResponse response = new BaseResponse();
		List<DeviceCommand> lst = mapCommand();

		deviceCommandRepo.deleteAll();

		response.setData(deviceCommandRepo.saveAll(lst));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/**
	 * mapping command from String[] to List<DeviceCommand>
	 * 
	 * @author tung
	 * @return BaseResponse
	 */
	public List<DeviceCommand> mapCommand() {
		List<DeviceCommand> lst = new ArrayList<>();
		for (int i = 0; i < DEFAULT_COMMAND.length; i++) {
			String[] tmp = DEFAULT_COMMAND[i].split(",");
			lst.add(new DeviceCommand(i + 1, tmp[0], tmp[1], tmp[2]));
		}

		return lst;
	}

	@Override
	public ResponseEntity<BaseResponse> findByType(String type) {
		BaseResponse response = new BaseResponse();
		if (Util.isNullOrEmpty(type)) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		List<DeviceCommand> lst = deviceCommandRepo.findByType(type);

		if (Util.isNull(lst)) {
			response.setData(new ArrayList<>());
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}

		response.setData(lst);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<BaseResponse> findExact(String type, String phrase) {
		BaseResponse response = new BaseResponse();
		if (Util.isNullOrEmpty(type) || Util.isNullOrEmpty(phrase)) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		List<DeviceCommand> lst = new ArrayList<>();

		if (Util.isNullOrEmpty(lst)) {
			response.setData(new ArrayList<>());
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}

		response.setData(lst.get(0));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<BaseResponse> createLircConfig(String command) {
		return null;
	}

	@Override
	public ResponseEntity<BaseResponse> upload(MultipartFile file) throws IOException, InvalidFormatException {
		String fileName = file.getOriginalFilename();
		if (!fileName.contains("xlsx")){
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		String filePath = System.getProperty("user.dir") + "upload" + fileName;
		file.transferTo(Paths.get(filePath));

		OPCPackage pkg = OPCPackage.open(filePath);

		XSSFWorkbook wb = new XSSFWorkbook(pkg);

		Sheet sheet = wb.getSheet("commands");
		if (sheet == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		List<DeviceCommand> lst = new ArrayList<>();

		Iterator<Row> rows = sheet.iterator();
		int count = 1;
		while (rows.hasNext()) {
			Row nextRow = rows.next();
			if (nextRow.getRowNum() == 0) {
				continue;
			}

			// Get all cells
			Iterator<Cell> cells = nextRow.cellIterator();
			DeviceCommand command = new DeviceCommand();
			command.setId(count);
			while (cells.hasNext()) {
				//Read cell
				Cell cell = cells.next();
				String cellValue = cell.getStringCellValue();
				if (cellValue == null || cellValue.toString().isEmpty()) {
					return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
				}
				// Set value for book object
				int columnIndex = cell.getColumnIndex();
				switch (columnIndex) {
					case COLUMN_INDEX_PRODUCT_TYPE:
						command.setType(cell.getStringCellValue());
					case COLUMN_INDEX_COMMAND:
						command.setCommand(cell.getStringCellValue());
					case COLUMN_INDEX_PHRASE_COMMAND:
						command.setPhraseCommand(cell.getStringCellValue());
					default:
						break;
				}
			}
			lst.add(command);
			count+=1;
		}
		deviceCommandRepo.deleteAll();
		return new ResponseEntity<>(new BaseResponse(deviceCommandRepo.saveAll(lst)),HttpStatus.OK);
	}

}
