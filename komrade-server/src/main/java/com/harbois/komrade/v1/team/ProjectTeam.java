package com.harbois.komrade.v1.team;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonView;
import com.harbois.common.auditing.Auditable;
import com.harbois.common.validation.annotations.NotNullable;
import com.harbois.common.validation.annotations.UpdatePolicy;
import com.harbois.komrade.constants.Entities;
import com.harbois.oauth.api.v1.common.Views;
import com.harbois.oauth.api.v1.common.domain.FunctionalId;

@Entity(name="PRJ_TEAM")
public class ProjectTeam implements Auditable<String>, FunctionalId{

	@Id
	@Column(name="TEAM_CODE")
	@JsonView(value= {Views.Add.class,Views.List.class})
	@NotNullable
	@UpdatePolicy(updateable=false)
	private String teamCode;
	@NotNullable	
	@Column(name="NAME")
	@JsonView(value= {Views.Add.class,Views.Update.class,Views.List.class})
	private String name;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getTeamCode() {
		return teamCode;
	}
	public void setTeamCode(String teamCode) {
		this.teamCode = teamCode;
	}
	@Override
	public String getObjectType() {
		return Entities.PROJECT_TEAM;
	}
	@Override
	public void setId(String id) {
		this.teamCode=id;
	}
	@Override
	public String getId() {
		return teamCode;
	}
}
