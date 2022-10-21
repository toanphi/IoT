package com.uet.iot.api;

import com.uet.iot.database.entity.DeviceData;
import com.uet.iot.request.DeviceDataDateFilterReq;
import com.uet.iot.res.LastSwitchDataReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.uet.iot.base.BaseResponse;
import com.uet.iot.business.ManageDeviceDataBusiness;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Api(value = "Manage device data APIs")
public class ManageDeviceDataApi {

    @Autowired
    private ManageDeviceDataBusiness manageDeviceDataBusiness;

    @ApiOperation(value = "get all devices data api", response = ResponseEntity.class)
    @RequestMapping(value = "/device/data", method = RequestMethod.GET)
    public ResponseEntity<BaseResponse> getAllDeviceData() {
        return manageDeviceDataBusiness.getAllDeviceData();
    }

    @ApiOperation(value = "save device data", response = ResponseEntity.class)
    @RequestMapping(value = "/device/data", method = RequestMethod.POST)
    public ResponseEntity<BaseResponse> saveDeviceData(@RequestBody DeviceData data) {
        return manageDeviceDataBusiness.saveDeviceData(data);
    }

    @ApiOperation(value = "get device data by device id", response = ResponseEntity.class)
    @RequestMapping(value = "/device/data/data", method = RequestMethod.POST)
    public ResponseEntity<BaseResponse> getDeviceDataById(
            @RequestBody DeviceDataDateFilterReq req) {
        return manageDeviceDataBusiness.getDeviceDataById(req.getDeviceId(), req.getStartDate(), req.getEndDate());
    }

    @ApiOperation(value = "get device data count by device id", response = ResponseEntity.class)
    @RequestMapping(value = "/device/data/count", method = RequestMethod.POST)
    public ResponseEntity<BaseResponse> getDeviceDataCountById(
            @RequestBody DeviceDataDateFilterReq req) {
        return manageDeviceDataBusiness.getDeviceDataCountById(req.getDeviceId(), req.getStartDate(), req.getEndDate());
    }

    @ApiOperation(value = "get last switch data", response = ResponseEntity.class)
    @RequestMapping(value = "/device/data/switch", method = RequestMethod.POST)
    public ResponseEntity<BaseResponse> getLastSwitchData(@RequestBody LastSwitchDataReq req) {
        return manageDeviceDataBusiness.getLastSwitchData(req.getControlTopic());
    }

}
