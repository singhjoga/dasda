package com.harbois.komrade.v1.team;

import org.springframework.data.jpa.repository.JpaRepository;

import com.harbois.komrade.v1.common.repositories.EntityRepository;

public interface ProjectTeamRepository extends JpaRepository<ProjectTeam, String>,EntityRepository<ProjectTeam, String>{

}
