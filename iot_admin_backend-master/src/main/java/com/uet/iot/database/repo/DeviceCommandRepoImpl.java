package com.uet.iot.database.repo;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;

import com.uet.iot.database.entity.DeviceCommand;

public class DeviceCommandRepoImpl implements DeviceCommandRepoCustom{

	@Autowired
	private EntityManager em;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<DeviceCommand> findByType(String type) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT d FROM DeviceCommand d");
		sb.append(" WHERE d.type = :type");
		
		Query qr = em.createQuery(sb.toString());
		
		qr.setParameter("type", type);
		
		return qr.getResultList();
		
//		Query qr = new Query();
//		qr.addCriteria(Criteria.where("type").is(type));
//		
//		return mongoTemplate.find(qr, DeviceCommand.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DeviceCommand> findExact(String type, String phrase) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT d FROM DeviceCommand d");
		sb.append(" WHERE d.type = :type");
		sb.append(" AND d.phraseCommand LIKE %:phrase%");
		
		Query qr = em.createQuery(sb.toString());
		
		qr.setParameter("type", type);
		qr.setParameter("phrase", phrase);
		
		return qr.getResultList();
	}

}
