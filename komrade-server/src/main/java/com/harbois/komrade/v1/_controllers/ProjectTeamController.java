package com.harbois.komrade.v1._controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.harbois.komrade.constants.Entities;
import com.harbois.komrade.v1.common.controllers.BaseEntityController;
import com.harbois.komrade.v1.team.ProjectTeam;
import com.harbois.komrade.v1.team.ProjectTeamService;
import com.harbois.oauth.security.Authorization;

@RestController
@RequestMapping("/api/v1/teams")
@Authorization(entity=Entities.PROJECT_TEAM)
public class ProjectTeamController extends BaseEntityController<ProjectTeam, String> {
	@Autowired
	public ProjectTeamController(ProjectTeamService service) {
		super(service);
	}
}
