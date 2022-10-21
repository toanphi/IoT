package com.uet.iot.api;

import com.uet.iot.base.BaseResponse;
import com.uet.iot.business.ManageLircConfigBusiness;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Api(value = "Manage Lirc Config APIs")
public class ManageLircConfigApi {

    @Autowired
    private ManageLircConfigBusiness manageLircConfigBusiness;

    @ApiOperation(value = "start recording process", response = ResponseEntity.class)
    @RequestMapping(value = "/lirc/{btnName}", method = RequestMethod.POST)
    public ResponseEntity<BaseResponse> record(@PathVariable String btnName) throws IOException {
        System.out.println("start recording btn");
        return manageLircConfigBusiness.record(btnName);
    }

    @ApiOperation(value = "init recording session", response = ResponseEntity.class)
    @RequestMapping(value = "/lirc/{deviceName}", method = RequestMethod.GET)
    public void initRecord(@PathVariable String deviceName) {
        manageLircConfigBusiness.initRecord(deviceName);
    }

    @ApiOperation(value = "init command line", response = ResponseEntity.class)
    @RequestMapping(value = "/lirc", method = RequestMethod.DELETE)
    public void finishRecord() throws IOException {
        manageLircConfigBusiness.finishRecord();
    }
}
