package com.uet.iot.database.repo;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.uet.iot.database.entity.Device;

public class DeviceRepoImpl implements DeviceRepoCustom{
	
	@Autowired
	private EntityManager em;

	@SuppressWarnings("unchecked")
	@Override
	public List<Device> findByName(String name) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT d FROM Device d");
		sb.append(" WHERE d.name = :name");
		
		Query qr = em.createQuery(sb.toString());
		qr.setParameter("name", name);
		
		List<Device> dev = qr.getResultList();
		
		return dev;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor = Exception.class)
	public void deleteDevice(int id) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("DELETE FROM Device d");
		sb.append(" WHERE d.id = :id");
		Query qr = em.createQuery(sb.toString());
		
		qr.setParameter("id", id);
		
		qr.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> findDistinctProductType() {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT DISTINCT d.productType FROM Device d");
		
		Query qr = em.createQuery(sb.toString());
		
		return qr.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Device> findById(int id) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT d FROM Device d");
		sb.append(" WHERE d.id = :id");
		
		Query qr = em.createQuery(sb.toString());
		qr.setParameter("id", id);
		
		List<Device> dev = qr.getResultList();
		
		return dev;
	}

	@Override
	public List<Device> findByControlTopic(String controlTopic) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT d FROM Device d");
		sb.append(" WHERE d.controlTopic = :controlTopic");
		sb.append(" ORDER BY d.gangs ASC");

		Query qr = em.createQuery(sb.toString());
		qr.setParameter("controlTopic", controlTopic);

		List<Device> dev = qr.getResultList();

		return dev;
	}

//	@Override
//	@Transactional
//	public void updateAutoIncrement() {
//		
//		StringBuilder sb = new StringBuilder();
//		sb.append("SELECT MAX(d.id) FROM Device d");
//		Query qr = em.createQuery(sb.toString());
//		
//		int lastId = (int) qr.getResultList().get(0);
//		
//		StringBuilder sb1 = new StringBuilder();
//		sb1.append("ALTER TABLE device AUTO_INCREMENT = :lastId");
//		
//		Query qr1 = em.createNativeQuery(sb1.toString());
//		qr1.setParameter("lastId", lastId);
//		
//		qr1.executeUpdate();
//		
//	}
}
