package net.devoat.common.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import com.fasterxml.jackson.annotation.JsonView;

import net.devoat.common.Views;

@MappedSuperclass
public abstract class BaseAuditableEntity{
	@Column(name="ADD_DATE")
	@JsonView(value=Views.List.class)
	private Date addDate;
	@Column(name="ADD_USER")
	@JsonView(value=Views.List.class)
	private String addUser;
	@Column(name="UPDATE_DATE")
	@JsonView(value=Views.List.class)
	private Date updateDate;
	@Column(name="UPDATE_USER")
	@JsonView(value=Views.List.class)
	private String updateUser;
	public Date getAddDate() {
		return addDate;
	}
	public void setAddDate(Date addDate) {
		this.addDate = addDate;
	}

	public String getAddUser() {
		return addUser;
	}
	public void setAddUser(String addUser) {
		this.addUser = addUser;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public String getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	
}
