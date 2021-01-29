package com.harbois.komrade.v1.pipelines;

import org.springframework.data.jpa.repository.JpaRepository;

import com.harbois.komrade.v1.common.repositories.EntityRepository;
import com.harbois.komrade.v1.pipelines.domain.Pipeline;

public interface PipelineRepository extends JpaRepository<Pipeline, String>,EntityRepository<Pipeline, String>{

}
