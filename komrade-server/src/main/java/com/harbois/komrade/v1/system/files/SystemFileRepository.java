package com.harbois.komrade.v1.system.files;

import org.springframework.data.jpa.repository.JpaRepository;

import com.harbois.komrade.v1.common.repositories.EntityRepository;

public interface SystemFileRepository extends JpaRepository<SystemFile, String>,EntityRepository<SystemFile, String>{

}
