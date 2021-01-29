package com.harbois.komrade.v1.resources;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.harbois.komrade.v1.common.services.BaseEntityService;
import com.harbois.komrade.v1.resources.domain.Host;
import com.harbois.komrade.v1.resources.repository.HostRepository;

@Service
public class HostService extends BaseEntityService<Host, String> {

	private static final Logger LOG = LoggerFactory.getLogger(HostService.class);

	private HostRepository repo;
	
	@Autowired
	public HostService(HostRepository repo) {
		super(repo, Host.class, String.class);
		this.repo=repo;
	}
}
