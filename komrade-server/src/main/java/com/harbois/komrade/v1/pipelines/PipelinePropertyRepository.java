package com.harbois.komrade.v1.pipelines;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.harbois.komrade.v1.common.repositories.EnvPropertyChildRepository;
import com.harbois.komrade.v1.pipelines.domain.PipelineProperty;

public interface PipelinePropertyRepository extends EnvPropertyChildRepository<PipelineProperty>, JpaRepository<PipelineProperty, String>{
	String QUERY1 = "SELECT * FROM PIPELINE_PROP WHERE PIPELINE_ID=?1 AND NAME=?2";
	@Query(value=QUERY1, nativeQuery=true)
	PipelineProperty findByName(String parentId, String name);

	String QUERY2 = "SELECT * FROM PIPELINE_PROP WHERE PIPELINE_ID=?1";
	@Query(value=QUERY2, nativeQuery=true)
	List<PipelineProperty> findAll(String parentId);
}
