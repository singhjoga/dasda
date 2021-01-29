package com.harbois.komrade.v1.pipelines;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.harbois.komrade.v1.common.repositories.EnvPropertyValueChildRepository;
import com.harbois.komrade.v1.pipelines.domain.PipelinePropertyValue;

public interface PipelinePropertyValueRepository extends EnvPropertyValueChildRepository<PipelinePropertyValue>, JpaRepository<PipelinePropertyValue, String>{
	String QUERY1 = "SELECT * FROM PIPELINE_PROP_VAL WHERE ENV_CODE IN ?2 AND PIPELINE_PROP_ID IN (SELECT PIPELINE_PROP_ID FROM PIPELINE_PROP WHERE PIPELINE_ID=?1)";
	@Query(value=QUERY1, nativeQuery=true)
	List<PipelinePropertyValue> findByParentIdAndEnvCodeIn(String pipelineId, Set<String> envCodes);
}
