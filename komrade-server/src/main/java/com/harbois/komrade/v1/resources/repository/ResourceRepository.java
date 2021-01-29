package com.harbois.komrade.v1.resources.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.harbois.komrade.v1.common.repositories.EntityRepository;
import com.harbois.komrade.v1.resources.domain.Resource;


public interface ResourceRepository extends JpaRepository<Resource, String>,EntityRepository<Resource, String>{

	List<Resource> findByTypeCodeOrderByName(String typeCode);
	default public List<Resource> search(String typeCode, String teamCode, String domainCode) {
		/*
		NativeQueryBuilder qb = new NativeQueryBuilder("SELECT * FROM res r", "r.name");
		qb.appendWhere(typeCode, "r.TYPE_CODE", "=");
		qb.appendWhere(teamCode, "r.TEAM_CODE", "=");
		qb.appendWhere(domainCode, "r.DOMAIN_CODE", "=");
		javax.persistence.Query query = qb.buildNativeQuery(EntityManagerProvider.getEntityManager(), Resource.class);
		List<Resource> result = query.getResultList();
		
		return result;
		*/
		return null;
	}
}
