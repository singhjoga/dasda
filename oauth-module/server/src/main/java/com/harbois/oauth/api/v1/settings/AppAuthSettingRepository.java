package com.harbois.oauth.api.v1.settings;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface AppAuthSettingRepository extends CrudRepository<AppAuthSetting, Long>{
	String QUERY1 = "SELECT * FROM APP_AUTH_SETTING WHERE IS_DISABLED=0 AND ( CLIENT_ID='global' OR CLIENT_ID=?1)";
	@Query(value=QUERY1, nativeQuery=true)
	List<AppAuthSetting> findAllByClientId(String clientId);
}
