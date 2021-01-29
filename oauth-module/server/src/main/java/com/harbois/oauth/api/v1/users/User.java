package com.harbois.oauth.api.v1.users;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.harbois.common.validation.annotations.NotNullable;
import com.harbois.common.validation.annotations.UpdatePolicy;
import com.harbois.oauth.api.v1.common.domain.AuditableEntity;

@Entity(name="USER")
public class User extends AuditableEntity<String>{

	@Id
	@NotNullable	
	@Column(name="USERNAME")
	private String name;
	@Column(name="FIRST_NAME")
	private String firstName;
	@Column(name="LAST_NAME")
	private String lastName;
	@Column(name="EMAIL")
	private String email;
	@Column(name="PASSWORD")
	@NotNullable
	@UpdatePolicy(updateable=false)
	private String password;
	@Column(name="IS_SYSTEM")
	private Boolean isSystem;
	@Column(name="IS_DISABLED")
	private Boolean isDisabled;	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Boolean getIsSystem() {
		return isSystem;
	}
	public void setIsSystem(Boolean isSystem) {
		this.isSystem = isSystem;
	}
	public Boolean getIsDisabled() {
		return isDisabled;
	}
	public void setIsDisabled(Boolean isDisabled) {
		this.isDisabled = isDisabled;
	}
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	@Override
	public String getId() {
		return name;
	}
	@Override
	public void setId(String id) {
		this.name=id;
	}
}
