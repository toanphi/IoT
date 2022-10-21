package com.uet.iot.business.impl;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.uet.iot.base.BaseResponse;
import com.uet.iot.business.ManageSupportedDeviceBusiness;
import com.uet.iot.database.entity.SupportedDevice;
import com.uet.iot.database.repo.SupportedDeviceRepo;
import com.uet.iot.util.Message;

@Service
public class ManageSupportedDeviceBusinessImpl implements ManageSupportedDeviceBusiness{

	private static final String URL = "http://lirc-remotes.sourceforge.net/remotes-table.html?fbclid=IwAR2Nyaz0emB40tum4zYIu_Mo3huokH77O6jxLFmgh9D-pTpPrH2vYkZsJIg";

	@Autowired
	private SupportedDeviceRepo supportedDeviceRepo;
	@Override
	public ResponseEntity<BaseResponse> fetchSupportedDevice() {
		Document doc = request(URL);
		List<String> finished = new ArrayList<>();
		
		if (doc == null) {
			return new ResponseEntity<>(new BaseResponse("wrong url"), HttpStatus.NOT_FOUND);
		}

		Elements exactTr = doc.select("table").get(2).select("tr");
		exactTr.remove(0);
		for (Element ele : exactTr) {
			String name = ele.select("td").get(3).text();
			if (!finished.contains(name)) {
				finished.add(name);
				supportedDeviceRepo.save(new SupportedDevice(name));
			}
		}

		return new ResponseEntity<>(new BaseResponse(Message.SUCCESS), HttpStatus.OK);
	}
	
	public Document request(String url) {
		try {
			Connection con = Jsoup.connect(url);
			Document doc = con.get();
			if (con.response().statusCode() == 200) {	
				return doc;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	@Override
	public ResponseEntity<BaseResponse> getAll() {
		return new ResponseEntity<>(new BaseResponse(supportedDeviceRepo.findAll()), HttpStatus.OK);
	}

}
