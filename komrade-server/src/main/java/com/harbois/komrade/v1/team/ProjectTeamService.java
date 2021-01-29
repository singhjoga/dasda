package com.harbois.komrade.v1.team;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.harbois.komrade.v1.common.services.BaseEntityService;

@Component
public class ProjectTeamService extends BaseEntityService<ProjectTeam, String>{
	@Autowired	
	public ProjectTeamService(ProjectTeamRepository repo) {
		super(repo,ProjectTeam.class, String.class);
	}
}
