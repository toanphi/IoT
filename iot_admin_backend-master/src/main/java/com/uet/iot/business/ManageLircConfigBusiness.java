package com.uet.iot.business;

import com.uet.iot.base.BaseResponse;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface ManageLircConfigBusiness {

    /**
     * init record session with device name
     * @param deviceName
     */
    ResponseEntity<Void> initRecord(String deviceName);

    /**
     * start record a single button signal
     * @param btnName
     * @return
     * @throws IOException
     */
    ResponseEntity<BaseResponse> record(String btnName) throws IOException;

    /**
     * end record session
     * @return
     */
    ResponseEntity<Void> finishRecord() throws IOException;
}
