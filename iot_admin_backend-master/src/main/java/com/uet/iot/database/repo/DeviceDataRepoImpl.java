package com.uet.iot.database.repo;

import com.uet.iot.database.entity.DeviceData;
import com.uet.iot.res.DeviceDataCount;
import com.uet.iot.util.Util;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.Date;
import java.util.List;

public class DeviceDataRepoImpl implements DeviceDataRepoCustom{

    @Autowired
    private EntityManager em;

    @Override
    public List<DeviceData> getDeviceDataById(int deviceId , Date startDate, Date endDate) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT d FROM DeviceData d");
        sb.append(" WHERE d.deviceId = :deviceId");

        if (!Util.isNullOrEmpty(startDate) && !Util.isNullOrEmpty(endDate)){
            sb.append(" AND d.createdDate >= :startDate");
            sb.append(" AND d.createdDate <= :endDate");
        }

        sb.append(" ORDER BY d.createdDate");

        Query qr = em.createQuery(sb.toString());

        qr.setParameter("deviceId", deviceId);
        if (!Util.isNullOrEmpty(startDate) && !Util.isNullOrEmpty(endDate)){
            qr.setParameter("startDate", startDate);
            qr.setParameter("endDate", endDate);
        }

        return qr.getResultList();
    }

    @Override
    public List<DeviceDataCount> getDeviceDataCountById(int deviceId) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select count(*) as count, data from device_data where device_id = :deviceId group by data");

        Query qr = em.createNativeQuery(sb.toString());
        qr.setParameter("deviceId", deviceId);

        qr.getResultList().forEach(d-> {
            d.toString();
        });

        return qr.getResultList();
    }

    @Override
    public DeviceData getLastSwitchCommand(int deviceId) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT d FROM DeviceData d");
        sb.append(" WHERE d.deviceId = :deviceId");
        sb.append(" ORDER BY d.createdDate DESC");

        Query qr = em.createQuery(sb.toString());
        qr.setParameter("deviceId", deviceId);
        qr.setMaxResults(1);

        List<DeviceData> lstDeviceData = qr.getResultList();
        if (lstDeviceData.isEmpty()) {
            return new DeviceData();
        }
        return lstDeviceData.get(0);
    }
}
