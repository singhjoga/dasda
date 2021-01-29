package com.harbois.oauth.server.api.v1.groups;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.TableGenerator;

import com.harbois.oauth.server.api.v1.common.domain.AuditableEntity;
import com.harbois.oauth.server.api.v1.common.domain.ClientIdentity;
import com.harbois.oauth.server.validation.annotations.NotNullable;
import com.harbois.oauth.server.validation.annotations.UpdatePolicy;

@Entity(name="USER_GROUP")
public class Group extends AuditableEntity<Long> implements ClientIdentity{

	@Id
	@TableGenerator(name = "USER_GROUP_ID", table= "APP_ID_GEN", initialValue = 100, allocationSize = 10)
    @GeneratedValue(generator = "USER_GROUP_ID")
	@Column(name="USER_GROUP_ID")
	@UpdatePolicy(insertable=false, updateable=false)	
	private Long id;
	@NotNullable	
	@Column(name="NAME")
	private String name;
	@Column(name="DESCRIPTION")
	@NotNullable
	private String description;
	@Column(name="IS_SYSTEM")
	private Boolean isSystem;
	@Column(name="IS_DISABLED")
	private Boolean isDisabled;
	@Column(name="CLIENT_ID")
	@NotNullable
	private String clientId;	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Boolean getIsSystem() {
		return isSystem;
	}
	public void setIsSystem(Boolean isSystem) {
		this.isSystem = isSystem;
	}

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Boolean getIsDisabled() {
		return isDisabled;
	}
	public void setIsDisabled(Boolean isDisabled) {
		this.isDisabled = isDisabled;
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

}
