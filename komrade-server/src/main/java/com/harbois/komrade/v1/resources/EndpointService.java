package com.harbois.komrade.v1.resources;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.harbois.komrade.v1.common.services.BaseEntityService;
import com.harbois.komrade.v1.resources.domain.Endpoint;
import com.harbois.komrade.v1.resources.repository.EndpointRepository;

@Service
public class EndpointService extends BaseEntityService<Endpoint, String> {

	private static final Logger LOG = LoggerFactory.getLogger(EndpointService.class);

	private EndpointRepository repo;
	
	@Autowired
	public EndpointService(EndpointRepository repo) {
		super(repo, Endpoint.class, String.class);
		this.repo=repo;
	}
}
